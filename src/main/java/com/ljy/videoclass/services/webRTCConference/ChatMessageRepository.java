package com.ljy.videoclass.services.webRTCConference;

import com.ljy.videoclass.services.webRTCConference.model.ChatMessage;

import java.util.List;

public interface ChatMessageRepository {
    void save(String classroomCode, ChatMessage chatMessage);
    List<ChatMessage> findAll(String classroomCode);
}
