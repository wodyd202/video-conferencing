package com.ljy.videoclass.services.rtcConference.presentation;

import com.google.gson.*;
import com.ljy.videoclass.services.rtcConference.application.RTCConferenceService;
import com.ljy.videoclass.services.rtcConference.application.RTCPanelistRegistry;
import com.ljy.videoclass.services.rtcConference.application.RTCPanelistService;
import com.ljy.videoclass.services.rtcConference.application.exception.ConferenceNotFoundException;
import com.ljy.videoclass.services.rtcConference.application.exception.NotMatchKeyException;
import com.ljy.videoclass.services.rtcConference.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@Profile("!test")
@Component
@AllArgsConstructor
public class ConferenceSocketHandler extends TextWebSocketHandler {
    private Gson gson;
    private RTCPanelistRegistry panelistRegistry;
    private RTCConferenceService rtcConferenceService;
    private RTCPanelistService rtcPanelistService;

    /**
     * @param session
     * @param status
     * # 회의실 퇴장
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        PanelistId panelistId = PanelistId.of(session.getPrincipal().getName());
        if(panelistRegistry.exist(panelistId)){
            ConferenceCode conferenceCode = ConferenceCode.of(getConferenceCodeBySessionUri(session));

            try {
                rtcConferenceService.leave(conferenceCode, panelistId);
            } catch (ConferenceNotFoundException e) {
                // 가능성 없음
            }
        }
    }

    /**
     * @param session
     * @param message
     * # 메시지 주고 받음
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
        ConferenceCode conferenceCode = ConferenceCode.of(getConferenceCodeBySessionUri(session));
        PanelistId panelistId = PanelistId.of(session.getPrincipal().getName());

        switch (getMessageType(jsonMessage)){
            case "open":
                handleOpen(panelistId, conferenceCode, session);
                break;
            case "join":
                handleJoin(jsonMessage, conferenceCode, session);
                break;
            case "expel":
                handleExpel(jsonMessage, conferenceCode, panelistId, session);
                break;
            case "shake":
                handleShake(conferenceCode, panelistId, session);
                break;
            case "chat":
                handleChat(jsonMessage, panelistId, conferenceCode, session);
                break;
            case "receiveVideoFrom":
                handleReceiveVideoFrom(jsonMessage, panelistId);
                break;
            case "onIceCandidate":
                handleIceCandidate(jsonMessage, panelistId);
                break;
        }
    }

    /**
     * @param panelistId
     * @param session
     * # 화상 회의 개설
     */
    private void handleOpen(PanelistId panelistId, ConferenceCode conferenceCode, WebSocketSession session) {
        rtcConferenceService.open(panelistId, conferenceCode, session);
    }

    /**
     * @param jsonMessage
     * @param panelistId
     * # 후보자가 등록되었을때
     */
    private void handleIceCandidate(JsonObject jsonMessage, PanelistId panelistId) {
        IceCandidate iceCandidate = createIceCandidateViaJsonObject(jsonMessage);
        PanelistId iceSender = PanelistId.of(jsonMessage.get("name").getAsString());
        rtcPanelistService.onIceCandidate(iceCandidate, panelistId, iceSender);
    }

    /**
     * @param jsonMessage
     * @param panelistId
     * # 다른 사용자에게 SDP 정보를 받음
     */
    private void handleReceiveVideoFrom(JsonObject jsonMessage, PanelistId panelistId) {
        PanelistId sender = PanelistId.of(jsonMessage.get("sender").getAsString());
        SdpInfo sdpInfo = new SdpInfo(jsonMessage.get("sdpOffer").getAsString());
        rtcPanelistService.receiveVideoFrom(sdpInfo, sender, panelistId);
    }

    /**
     * @param jsonMessage
     * @param panelistId
     * @param conferenceCode
     * @param session
     * # 채팅
     */
    private void handleChat(JsonObject jsonMessage, PanelistId panelistId, ConferenceCode conferenceCode, WebSocketSession session) throws IOException {
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(panelistId.get())
                .message(jsonMessage.get("message").getAsString())
                .build();
        try {
            rtcConferenceService.sendChatMessage(conferenceCode, chatMessage);
        } catch (ConferenceNotFoundException e) {
            sendMessage(session, "notFound");
        }
    }

    /**
     * @param conferenceCode
     * @param panelistId
     * @param session
     * # 손 흔들기
     */
    private void handleShake(ConferenceCode conferenceCode, PanelistId panelistId, WebSocketSession session) throws IOException {
        try {
            rtcConferenceService.shake(conferenceCode, panelistId);
        } catch (ConferenceNotFoundException e) {
            sendMessage(session, "notFound");
        }
    }

    /**
     * @param jsonMessage
     * @param conferenceCode
     * @param session
     * # 회의 참여
     */
    private void handleJoin(JsonObject jsonMessage, ConferenceCode conferenceCode, WebSocketSession session) throws IOException {
        try {
            ConferenceKey conferenceKey = getConferencekey(jsonMessage);
            rtcConferenceService.join(conferenceCode, PanelistId.of(session.getPrincipal().getName()), conferenceKey, session);
        } catch (NotMatchKeyException e) {
            sendMessage(session, "notMatchKey");
            // 사용자가 가져온 키값이 일치하지 않으면 Exception
        } catch (ConferenceNotFoundException e) {
            sendMessage(session, "notFound");
        }
    }

    /**
     * @param jsonMessage
     * @param conferenceCode
     * @param panelistId
     * @param session
     * # 회의에서 추방
     */
    private void handleExpel(JsonObject jsonMessage, ConferenceCode conferenceCode, PanelistId panelistId, WebSocketSession session) throws IOException {
        PanelistId targetPanelistId = PanelistId.of(jsonMessage.get("targetPanelistId").getAsString());
        try {
            rtcConferenceService.expel(conferenceCode, targetPanelistId, panelistId);
        } catch (ConferenceNotFoundException e) {
            sendMessage(session, "notFound");
        }
    }

    private void sendMessage(WebSocketSession session, String id) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        session.sendMessage(new TextMessage(jsonObject.toString()));
    }

    private ConferenceKey getConferencekey(JsonObject jsonMessage) {
        return jsonMessage.get("key") == null ? null : ConferenceKey.of(jsonMessage.get("key").getAsString());
    }

    private String getMessageType(JsonObject jsonMessage){
        return jsonMessage.get("id").getAsString();
    }

    private IceCandidate createIceCandidateViaJsonObject(JsonObject jsonObject){
        JsonObject candidate = jsonObject.get("candidate").getAsJsonObject();
        final String CANDIDATE = candidate.get("candidate").getAsString();
        final String SDP_MID = candidate.get("sdpMid").getAsString();
        final int SDP_M_LINE_INDEX = candidate.get("sdpMLineIndex").getAsInt();

        return new IceCandidate(CANDIDATE, SDP_MID, SDP_M_LINE_INDEX);
    }

    private String getConferenceCodeBySessionUri(WebSocketSession session) {
        String uri = session.getUri().toString();
        int lastSlashIdx = uri.lastIndexOf("/");
        String conferenceCode = uri.substring(lastSlashIdx + 1);
        return conferenceCode;
    }
}
