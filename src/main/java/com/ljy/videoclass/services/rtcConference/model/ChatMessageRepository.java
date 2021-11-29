package com.ljy.videoclass.services.rtcConference.model;

import java.util.List;

public interface ChatMessageRepository {
    void save(String conferenceCode, ChatMessage chatMessage);
    List<ChatMessage> findAll(String conferenceCode);
}
