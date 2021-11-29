package com.ljy.videoclass.services.rtcConference.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.MediaPipeline;
import org.springframework.web.socket.WebSocketSession;

import java.io.Closeable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class RTCConference implements Closeable {
    private String creatorId;
    private String code;
    private LocalDateTime createDateTime;

    private ConcurrentMap<String, RTCPanelist> joinPanelists = new ConcurrentHashMap<>();
    private MediaPipeline pipeline;

    public RTCConference(String creator, String code, MediaPipeline pipeline) {
        this.creatorId = creator;
        this.code = code;
        this.createDateTime = LocalDateTime.now();
        this.pipeline = pipeline;
    }

    /**
     * @param joinerSession
     * # 회의 참여
     */
    public RTCPanelist join(WebSocketSession joinerSession){
        RTCPanelist joiner = createPanelistSession(joinerSession);
        log.info("join confenrece : {}", joiner.getPanelistId());

        final JsonObject newParticipantMsg = new JsonObject();
        newParticipantMsg.addProperty("id", "newPanelistArrived");
        newParticipantMsg.addProperty("name", joiner.getPanelistId());

        final JsonArray panelists = new JsonArray();

        // 기존 회의자들에게 메시지 보냄
        final List<String> panelistList = new ArrayList<>(joinPanelists.values().size());
        for(RTCPanelist panelist : joinPanelists.values()){
            panelist.sendMessage(newParticipantMsg);
            panelistList.add(panelist.getPanelistId());
            panelists.add(new JsonPrimitive(panelist.getPanelistId()));
        }

        // 회의 정보 보냄
        sendConferenceInfoMsg(joiner);

        // 기존 회의 참여자들 보냄
        sendExistingPanelistMsg(joiner, panelists);

        // 회의실 입장
        joinPanelists.put(joiner.getPanelistId(), joiner);
        return joiner;
    }

    private void sendConferenceInfoMsg(RTCPanelist joiner) {
        final JsonObject conferenceInfoMsg = new JsonObject();
        conferenceInfoMsg.addProperty("id", "conferenceInfo");
        conferenceInfoMsg.addProperty("cod", code);
        conferenceInfoMsg.addProperty("creator", creatorId);
        conferenceInfoMsg.addProperty("createDateTime", createDateTime.toString());

        joiner.sendMessage(conferenceInfoMsg);
    }

    private void sendExistingPanelistMsg(RTCPanelist joiner, JsonArray panelists) {
        final JsonObject existingPanelistMsg = new JsonObject();
        existingPanelistMsg.addProperty("id", "existingPanelists");
        existingPanelistMsg.add("data", panelists);

        joiner.sendMessage(existingPanelistMsg);
    }

    private RTCPanelist createPanelistSession(WebSocketSession joinerSession) {
        return new RTCPanelist(joinerSession.getPrincipal().getName(), joinerSession, pipeline);
    }

    /**
     * @param panelist
     * # 회의 퇴장
     */
    public void leave(RTCPanelist panelist){
        joinPanelists.remove(panelist.getPanelistId());

        final JsonObject participantLeftJson = new JsonObject();
        participantLeftJson.addProperty("id", "panelistLeft");
        participantLeftJson.addProperty("name", panelist.getPanelistId());

        for(RTCPanelist existingPanelist : joinPanelists.values()){
            existingPanelist.cancelVideoFrom(panelist);
            existingPanelist.sendMessage(participantLeftJson);
        }
    }

    /**
     * # 회의를 진행중인 모든 사람들에게 메시지 보냄
     */
    public void noticeAll(JsonObject jsonMessage) {
        for(RTCPanelist existingPanelist : joinPanelists.values()){
            existingPanelist.sendMessage(jsonMessage);
        }
    }

    /**
     * @param targetPanelistId
     * @param panelist
     * # 추방
     */
    public void expel(String targetPanelistId, RTCPanelist panelist) {
        if(hasExpelRole(panelist)){
            RTCPanelist targetPanelist = joinPanelists.get(targetPanelistId);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", "expel");
            targetPanelist.sendMessage(jsonObject);
            log.info("expel panelist : {}", targetPanelistId);
            return;
        }
        log.warn("no has role expel : {}", panelist.getPanelistId());
    }

    private boolean hasExpelRole(RTCPanelist panelist) {
        return panelist.getPanelistId().equals(creatorId);
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public void close() throws IOException {
        for(RTCPanelist panelist : joinPanelists.values()){
            panelist.close();
        }
    }
}
