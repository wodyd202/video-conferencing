package com.ljy.videoclass.services.rtcConference.application;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ljy.videoclass.services.rtcConference.application.event.ClosedConferenceEvent;
import com.ljy.videoclass.services.rtcConference.application.event.OveredMessageCountEvent;
import com.ljy.videoclass.services.rtcConference.application.event.RemovedConferenceMessageStoreEvent;
import com.ljy.videoclass.services.rtcConference.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RTCChatService {
    @Autowired private RTCPanelistRegistry panelistRegistry;
    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private ApplicationEventPublisher applicationEventPublisher;

    /**
     * @param conference
     * @param panelistId
     * # 이전 메시지 전송
     */
    public void sendBeforeMessage(RtcConference conference, PanelistId panelistId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAll(conference.getCode(), 0, -1);
        JsonObject jsonObject = chatMessages2jsonArray(chatMessages);

        // 회의 진행중인 회의자 정보 가져옴
        RtcPanelist panelist = panelistRegistry.get(panelistId);
        panelist.sendMessage(jsonObject);
        log.info("send before message to : {}", panelistId);
    }

    private JsonObject chatMessages2jsonArray(List<ChatMessage> chatMessages) {
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
        return jsonObject;
    }

    /**
     * @param conferenceCode
     * @param chatMessage
     * # 메시지 저장
     */
    public void persist(ConferenceCode conferenceCode, ChatMessage chatMessage) {
        chatMessageRepository.save(conferenceCode, chatMessage);
        log.info("save chat message into redis : {}", chatMessage);

        if(isOverMessageCount(conferenceCode)){
            List<ChatMessage> chatMessages = chatMessageRepository.remove(conferenceCode, 60);
            applicationEventPublisher.publishEvent(new OveredMessageCountEvent(conferenceCode, chatMessages));
        }
    }

    private boolean isOverMessageCount(ConferenceCode conferenceCode){
        return chatMessageRepository.countAll(conferenceCode) >= 80;
    }

    /**
     * @param event
     * # 회의실 제거 이벤트 핸들러
     */
    @EventListener
    protected void handleRemovedConferenceEvent(ClosedConferenceEvent event){
        ConferenceCode conferenceCode = ConferenceCode.of(event.getConferenceCode());
        List<ChatMessage> chatMessages = chatMessageRepository.remove(conferenceCode);
        applicationEventPublisher.publishEvent(new RemovedConferenceMessageStoreEvent(conferenceCode, chatMessages));
    }
}

