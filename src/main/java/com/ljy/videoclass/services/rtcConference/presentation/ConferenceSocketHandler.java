package com.ljy.videoclass.services.rtcConference.presentation;

import com.google.gson.*;
import com.ljy.videoclass.services.rtcConference.application.RTCConferenceService;
import com.ljy.videoclass.services.rtcConference.application.RTCPanelistService;
import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
import com.ljy.videoclass.services.rtcConference.model.ChatMessage;
import com.ljy.videoclass.services.rtcConference.model.PanelistId;
import com.ljy.videoclass.services.rtcConference.model.SdpInfo;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@Profile("!test")
public class ConferenceSocketHandler extends TextWebSocketHandler {
    private static final Gson gson = new GsonBuilder().create();
    @Autowired private RTCConferenceService rtcConferenceService;
    @Autowired private RTCPanelistService rtcPanelistService;

    /**
     * @param session
     * # 회의실 입장
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        ConferenceCode conferenceCode = ConferenceCode.of(getConferenceCodeBySessionUri(session));

        rtcConferenceService.join(conferenceCode, PanelistId.of(session.getPrincipal().getName()), session);
    }

    /**
     * @param session
     * @param status
     * # 회의실 퇴장
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        ConferenceCode conferenceCode = ConferenceCode.of(getConferenceCodeBySessionUri(session));

        rtcConferenceService.leave(conferenceCode, PanelistId.of(session.getPrincipal().getName()));
    }

    /**
     * @param session
     * @param message
     * # 메시지 주고 받음
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
        ConferenceCode conferenceCode = ConferenceCode.of(getConferenceCodeBySessionUri(session));
        PanelistId panelistId = PanelistId.of(session.getPrincipal().getName());

        switch (getMessageType(jsonMessage)){
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
