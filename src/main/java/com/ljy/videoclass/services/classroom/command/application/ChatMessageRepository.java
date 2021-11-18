package com.ljy.videoclass.services.classroom.command.application;

import com.ljy.videoclass.services.classroom.command.infrastructure.model.ChatMessage;

import java.util.List;

public interface ChatMessageRepository {
    void save(String classroomCode, ChatMessage chatMessage);
    List<ChatMessage> findAll(String classroomCode);
}
