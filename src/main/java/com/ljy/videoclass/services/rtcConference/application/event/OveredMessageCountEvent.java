package com.ljy.videoclass.services.rtcConference.application.event;

import com.ljy.videoclass.services.rtcConference.model.ChatMessage;
import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
import lombok.Getter;

import java.util.List;

@Getter
public class OveredMessageCountEvent extends ConferenceEvent {
    private List<ChatMessage> chatMessages;

    public OveredMessageCountEvent(ConferenceCode conferenceCode, List<ChatMessage> chatMessages) {
        super(conferenceCode.get());
        this.chatMessages = chatMessages;
    }
}
