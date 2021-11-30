package com.ljy.videoclass.services.rtcConference.application.event;

import com.ljy.videoclass.services.rtcConference.model.ChatMessage;
import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
import lombok.Getter;

import java.util.List;

@Getter
public class RemovedConferenceMessageStoreEvent extends ConferenceEvent {
    private List<ChatMessage> chatMessages;

    public RemovedConferenceMessageStoreEvent(ConferenceCode conferenceCode, List<ChatMessage> chatMessages) {
        super(conferenceCode.get());
        this.chatMessages = chatMessages;
    }
}
