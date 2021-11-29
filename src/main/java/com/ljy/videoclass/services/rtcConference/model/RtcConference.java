package com.ljy.videoclass.services.rtcConference.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.MediaPipeline;
import org.springframework.web.socket.WebSocketSession;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class RtcConference implements Closeable {
    private PanelistId creatorId;
    private ConferenceCode code;
    private LocalDateTime createDateTime;

    private ConcurrentMap<PanelistId, RtcPanelist> joinPanelists = new ConcurrentHashMap<>();
    private MediaPipeline pipeline;

    public RtcConference(PanelistId creator, ConferenceCode code, MediaPipeline pipeline) {
        this.creatorId = creator;
        this.code = code;
        this.createDateTime = LocalDateTime.now();
        this.pipeline = pipeline;
    }

    /**
     * @param joiner 참여자
     * # 회의 참여
     */
    public void join(RtcPanelist joiner){
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
        conferenceInfoMsg.addProperty("cod", code.get());
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

    public RtcPanelist getPermissionToParticipate(PanelistId joinerId, WebSocketSession joinerSession) {
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
