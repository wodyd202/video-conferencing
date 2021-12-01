package com.ljy.videoclass.services.rtcConference.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ljy.videoclass.services.rtcConference.application.exception.NotMatchKeyException;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.MediaPipeline;
import org.springframework.web.socket.WebSocketSession;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class RtcConference implements Closeable {
    private PanelistId creatorId;
    private ConferenceCode code;
    private ConferenceKey key;
    private LocalDateTime createDateTime;

    private ConcurrentMap<PanelistId, RtcPanelist> joinPanelists = new ConcurrentHashMap<>();
    private MediaPipeline pipeline;

    public RtcConference(ConferenceCode code, MediaPipeline pipeline) {
        this.code = code;
        this.key = createKey();
        this.createDateTime = LocalDateTime.now();
        this.pipeline = pipeline;
    }

    private ConferenceKey createKey() {
        return ConferenceKey.of(UUID.randomUUID().toString().substring(0,8));
    }

    /**
     * @param permissionValidator
     * # 회의 개설
     */
    public void open(PermissionValidator permissionValidator, RtcPanelist creator){
        permissionValidator.validation(creator.getPanelistId());
        creatorId = creator.getPanelistId();

        sendConferenceInfoMsg(creator);

        sendExistingPanelistMsg(creator, new JsonArray());
        joinPanelists.put(creator.getPanelistId(), creator);
    }

    /**
     * @param joiner 참여자
     */
    public void join(RtcPanelist joiner) {
        final JsonObject newParticipantMsg = createNewParticipantMsg(joiner);

        // 기존 회의자들에게 메시지 보내고 기존 회의자 목록 가져옴
        final JsonArray panelists = new JsonArray();
        for(RtcPanelist panelist : joinPanelists.values()){
            panelist.sendMessage(newParticipantMsg);
            panelists.add(new JsonPrimitive(panelist.getPanelistId().get()));
        }

        // 회의 정보 보냄
        sendConferenceInfoMsg(joiner);

        // 기존 회의 참여자들 정보를 보냄
        sendExistingPanelistMsg(joiner, panelists);

        // 회의실 입장
        joinPanelists.put(joiner.getPanelistId(), joiner);
    }

    private boolean matchKey(ConferenceKey key) {
        return this.key.equals(key);
    }

    private JsonObject createNewParticipantMsg(RtcPanelist joiner){
        final JsonObject newParticipantMsg = new JsonObject();
        newParticipantMsg.addProperty("id", "newPanelistArrived");
        newParticipantMsg.addProperty("name", joiner.getPanelistId().get());
        return newParticipantMsg;
    }

    private void sendConferenceInfoMsg(RtcPanelist joiner) {
        joiner.sendMessage(createConferenceInfoMsg());
    }

    private JsonObject createConferenceInfoMsg(){
        final JsonObject conferenceInfoMsg = new JsonObject();
        conferenceInfoMsg.addProperty("id", "conferenceInfo");
        conferenceInfoMsg.addProperty("code", code.get());
        conferenceInfoMsg.addProperty("key", key.get());
        conferenceInfoMsg.addProperty("creator", creatorId.get());
        conferenceInfoMsg.addProperty("createDateTime", createDateTime.toString());
        return conferenceInfoMsg;
    }

    private void sendExistingPanelistMsg(RtcPanelist joiner, JsonArray panelists) {
        joiner.sendMessage(createExistingPanelistMsg(panelists));
    }

    private JsonObject createExistingPanelistMsg(JsonArray panelists){
        final JsonObject existingPanelistMsg = new JsonObject();
        existingPanelistMsg.addProperty("id", "existingPanelists");
        existingPanelistMsg.add("data", panelists);
        return existingPanelistMsg;
    }

    public RtcPanelist getCreator(PermissionValidator permissionValidator,
                                  PanelistId creatorId,
                                  WebSocketSession creatorSession){
        permissionValidator.validation(creatorId);
        return new RtcPanelist(creatorId, creatorSession, pipeline);
    }

    public RtcPanelist getPermissionToParticipate(PermissionValidator permissionValidator,
                                                  PanelistId joinerId,
                                                  ConferenceKey key,
                                                  WebSocketSession joinerSession) throws NotMatchKeyException {
        if(!matchKey(key)){
            throw new NotMatchKeyException();
        }
        permissionValidator.validation(joinerId);
        return new RtcPanelist(joinerId, joinerSession, pipeline);
    }

    /**
     * @param panelist
     * # 회의 퇴장
     */
    public void leave(RtcPanelist panelist) {
        // 회의자 목록에서 제거
        joinPanelists.remove(panelist.getPanelistId());

        final JsonObject participantLeftJson = createParticipantLeftMsg(panelist);
        for(RtcPanelist existingPanelist : joinPanelists.values()){
            existingPanelist.cancelVideoFrom(panelist);
            existingPanelist.sendMessage(participantLeftJson);
        }

        panelist.close();

        // 회의장이 나간 경우 다음 사람에게 회의장 인계
        if(existAnyJoiner() && isCreatorLeave(panelist)){
            PanelistId panelistId = passCreatorRole();
            noticeAll(createPassCreatorRoleJson(panelistId));
        }
    }

    private boolean existAnyJoiner() {
        return !joinPanelists.isEmpty();
    }

    private boolean isCreatorLeave(RtcPanelist panelist) {
        return creatorId.equals(panelist.getPanelistId());
    }

    private PanelistId passCreatorRole() {
        Set<PanelistId> joinerListSet = joinPanelists.keySet();
        for (PanelistId panelistId : joinerListSet) {
            RtcPanelist rtcPanelist = joinPanelists.get(panelistId);
            changeCreator(rtcPanelist);
            return rtcPanelist.getPanelistId();
        }
        return null;
    }

    private JsonObject createPassCreatorRoleJson(PanelistId panelistId){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "passCreatorRole");
        jsonObject.addProperty("to", panelistId.get());
        return jsonObject;
    }

    private void changeCreator(RtcPanelist rtcPanelist) {
        log.info("change creator {} to {}", creatorId, rtcPanelist.getPanelistId());
        creatorId = rtcPanelist.getPanelistId();
    }

    private JsonObject createParticipantLeftMsg(RtcPanelist panelist){
        final JsonObject participantLeftJson = new JsonObject();
        participantLeftJson.addProperty("id", "panelistLeft");
        participantLeftJson.addProperty("name", panelist.getPanelistId().get());
        return participantLeftJson;
    }

    /**
     * # 회의를 진행중인 모든 사람들에게 메시지 보냄
     */
    public void noticeAll(JsonObject jsonMessage) {
        for(RtcPanelist existingPanelist : joinPanelists.values()){
            existingPanelist.sendMessage(jsonMessage);
        }
    }

    /**
     * @param targetPanelist 타겟 회의자 아이디
     * @param who 추방자
     * # 추방
     */
    public void expel(RtcPanelist targetPanelist, RtcPanelist who) {
        if(hasExpelRole(who)){
            JsonObject jsonObject = createExpelMsg();
            targetPanelist.sendMessage(jsonObject);
            log.info("expel panelist : {}", targetPanelist);
            return;
        }
        log.warn("no has role expel : {}", who.getPanelistId());
    }

    private JsonObject createExpelMsg(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "expel");
        return jsonObject;
    }

    /**
     * @param panelist
     * # 흔들기
     */
    public void shake(RtcPanelist panelist) {
        JsonObject jsonObject = createShakeMsg(panelist);
        noticeAll(jsonObject);
    }

    private JsonObject createShakeMsg(RtcPanelist panelist) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "shake");
        jsonObject.addProperty("sender", panelist.getPanelistId().get());
        return jsonObject;
    }

    private boolean hasExpelRole(RtcPanelist panelist) {
        return panelist.getPanelistId().equals(creatorId);
    }

    /**
     * @param chatMessage
     * # 채팅 메시지 보내기
     */
    public void sendChat(ChatMessage chatMessage) {
        noticeAll(createChatMsg(chatMessage));
    }

    private JsonObject createChatMsg(ChatMessage chatMessage){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "chat");
        jsonObject.addProperty("message", chatMessage.getMessage());
        jsonObject.addProperty("sender", chatMessage.getSender());
        return jsonObject;
    }

    public boolean isClose(){
        return joinPanelists.isEmpty();
    }

    public ConferenceCode getCode() {
        return code;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public PanelistId getCreatorId() {
        return creatorId;
    }

    @Override
    public void close() {
        for(RtcPanelist panelist : joinPanelists.values()){
            panelist.close();
        }
    }
}
