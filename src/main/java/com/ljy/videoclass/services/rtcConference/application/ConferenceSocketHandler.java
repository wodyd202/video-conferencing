package com.ljy.videoclass.services.rtcConference.application;

import com.google.gson.*;
import com.ljy.videoclass.services.rtcConference.model.ChatMessage;
import com.ljy.videoclass.services.rtcConference.model.ChatMessageRepository;
import com.ljy.videoclass.services.rtcConference.model.RTCConference;
import com.ljy.videoclass.services.rtcConference.model.RTCPanelist;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Slf4j
@Component
public class ConferenceSocketHandler extends TextWebSocketHandler {
    private static final Gson gson = new GsonBuilder().create();
    @Autowired private ConferenceManager conferenceManager;
    @Autowired private PanelistRegistry panelistRegistry;

    @Autowired private ChatMessageRepository chatMessageRepository;

    /**
     * @param session
     * # 회의실 입장
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String conferenceCode = getConferenceCodeBySessionUri(session);
        RTCConference conference = getConference(session);

        // 회의 참여
        RTCPanelist joiner = conference.join(session);

        panelistRegistry.register(joiner);
        log.info("join panelist into {} conference : ", conferenceCode);

        // 이전 메시지 전송
        sendBeforeMessage(conference, joiner);
    }

    // 회의 가져옴
    // 만약 회의가 존재하지 않으면 새로 생성
    private RTCConference getConference(WebSocketSession session) {
        String conferenceCode = getConferenceCodeBySessionUri(session);
        String panelistId = session.getPrincipal().getName();

        return conferenceManager.getByCode(conferenceCode).orElseGet(()-> {
            log.info("create conference : {}", conferenceCode);
            return conferenceManager.createByCode(conferenceCode, panelistId);
        });
    }

    // 이전 메시지 전송
    private void sendBeforeMessage(RTCConference conference, RTCPanelist joiner) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAll(conference.getCode());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "beforeMessage");
        JsonArray jsonArray = new JsonArray();

        chatMessages.forEach(chat->{
            JsonObject chatJson = new JsonObject();
            chatJson.addProperty("sender", chat.getSender());
            chatJson.addProperty("message", chat.getMessage());
            chatJson.addProperty("sendDate", chat.getSendDate().toString());
            jsonArray.add(chatJson);
        });

        jsonObject.add("messages", jsonArray);
        joiner.sendMessage(jsonObject);
    }

    /**
     * @param session
     * @param status
     * # 회의실 퇴장
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String conferenceCode = getConferenceCodeBySessionUri(session);
        String panelistId = session.getPrincipal().getName();
        RTCPanelist panelist = panelistRegistry.remove(panelistId);
        panelist.close();
        conferenceManager.getByCode(conferenceCode).get().leave(panelist);
        log.info("{} leave the {} conference", panelistId, conferenceCode);
    }

    /**
     * @param session
     * @param message
     * # 메시지 주고 받음
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
        String panelistId = session.getPrincipal().getName();
        RTCPanelist panelist = panelistRegistry.get(panelistId);

        switch (getMessageType(jsonMessage)){
            case "expel":
                expel(session, jsonMessage, panelist);
                break;
            case "shake":
                shake(session, jsonMessage, panelist);
                break;
            case "chat":
                chat(session, jsonMessage, panelist);
                break;
            case "receiveVideoFrom":
                receiveVideoFrom(jsonMessage, panelist);
                break;
            case "onIceCandidate":
                onIceCandidate(jsonMessage, panelist);
                break;
        }
    }

    private void expel(WebSocketSession session,JsonObject jsonMessage, RTCPanelist panelist) {
        RTCConference conference = conferenceManager.getByCode(getConferenceCodeBySessionUri(session)).get();
        conference.expel(jsonMessage.get("targetPanelistId").getAsString(), panelist);
    }

    private String getMessageType(JsonObject jsonMessage){
        return jsonMessage.get("id").getAsString();
    }

    // handle shake
    private void shake(WebSocketSession session, JsonObject jsonMessage, RTCPanelist panelist) {
        noticeAll(session, jsonMessage, panelist);
        log.info("shake : {}", jsonMessage);
    }

    // handle chat
    private void chat(WebSocketSession session, JsonObject jsonMessage, RTCPanelist panelist){
        noticeAll(session, jsonMessage, panelist);
        saveChatMessage(session, jsonMessage, panelist);
        log.info("send message : {}", jsonMessage);
    }

    private void noticeAll(WebSocketSession session, JsonObject jsonMessage, RTCPanelist panelist) {
        // 현재 진행중인 회의 가져옴
        RTCConference conference = conferenceManager.getByCode(getConferenceCodeBySessionUri(session)).get();

        jsonMessage.addProperty("sender", panelist.getPanelistId());
        conference.noticeAll(jsonMessage);
    }

    // 채팅 메시지 저장
    private void saveChatMessage(WebSocketSession session, JsonObject jsonMessage, RTCPanelist panelist) {
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(panelist.getPanelistId())
                .message(jsonMessage.get("message").getAsString())
                .build();
        chatMessageRepository.save(getConferenceCodeBySessionUri(session), chatMessage);
    }


    // handle receiveVideoFrom
    private void receiveVideoFrom(JsonObject jsonMessage, RTCPanelist panelist){
        final String SENDER = jsonMessage.get("sender").getAsString();
        final String SDP_OFFER = jsonMessage.get("sdpOffer").getAsString();

        RTCPanelist senderPanelist = panelistRegistry.get(SENDER);
        panelist.receiveVideoFrom(senderPanelist, SDP_OFFER);
    }

    // handle onIceCandidate
    private void onIceCandidate(JsonObject jsonMessage, RTCPanelist panelist) {
        final String NAME = jsonMessage.get("name").getAsString();

        IceCandidate iceCandidate = createIceCandidateViaJsonObject(jsonMessage);
        panelist.addCantidate(iceCandidate, NAME);
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
