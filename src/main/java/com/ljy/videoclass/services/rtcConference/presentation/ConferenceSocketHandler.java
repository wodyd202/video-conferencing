package com.ljy.videoclass.services.rtcConference.presentation;

import com.google.gson.*;
import com.ljy.videoclass.services.rtcConference.application.RTCConferenceService;
import com.ljy.videoclass.services.rtcConference.application.RTCPanelistRegistry;
import com.ljy.videoclass.services.rtcConference.application.RTCPanelistService;
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

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {}

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

            rtcConferenceService.leave(conferenceCode, panelistId);
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
            case "join":
                try {
                    ConferenceKey conferenceKey = getConferencekey(jsonMessage);
                    rtcConferenceService.join(conferenceCode, PanelistId.of(session.getPrincipal().getName()), conferenceKey, session);
                } catch (NotMatchKeyException e) {
                    // 사용자가 가져온 키값이 일치하지 않으면 Exception
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", "noMatchKey");
                    session.sendMessage(new TextMessage(jsonObject.toString()));
                }
                break;
            case "expel":
                PanelistId targetPanelistId = PanelistId.of(jsonMessage.get("targetPanelistId").getAsString());
                rtcConferenceService.expel(conferenceCode, targetPanelistId, panelistId);
                break;
            case "shake":
                rtcConferenceService.shake(conferenceCode, panelistId);
                break;
            case "chat":
                ChatMessage chatMessage = ChatMessage.builder()
                        .sender(panelistId.get())
                        .message(jsonMessage.get("message").getAsString())
                        .build();
                rtcConferenceService.sendChatMessage(conferenceCode, chatMessage);
                break;
            case "receiveVideoFrom":
                PanelistId sender = PanelistId.of(jsonMessage.get("sender").getAsString());
                SdpInfo sdpInfo = new SdpInfo(jsonMessage.get("sdpOffer").getAsString());
                rtcPanelistService.receiveVideoFrom(sdpInfo, sender, panelistId);
                break;
            case "onIceCandidate":
                IceCandidate iceCandidate = createIceCandidateViaJsonObject(jsonMessage);
                PanelistId iceSender = PanelistId.of(jsonMessage.get("name").getAsString());
                rtcPanelistService.onIceCandidate(iceCandidate, panelistId, iceSender);
                break;
        }
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
