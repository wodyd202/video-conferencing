package com.ljy.videoclass.services.rtcConference.application;

import com.ljy.videoclass.services.rtcConference.model.ChatMessage;
import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;

import java.util.List;

public interface ChatMessageRepository {
    void save(ConferenceCode conferenceCode, ChatMessage chatMessage);
    List<ChatMessage> findAll(ConferenceCode conferenceCode, int start, int end);
    long countAll(ConferenceCode conferenceCode);
    List<ChatMessage> remove(ConferenceCode conferenceCode, int count);
    List<ChatMessage> remove(ConferenceCode conferenceCode);
}
