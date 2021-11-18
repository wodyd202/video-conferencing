package com.ljy.videoclass.services.classroom.infrastructure;

import com.ljy.videoclass.services.classroom.command.application.ChatMessageRepository;
import com.ljy.videoclass.services.classroom.command.infrastructure.model.ChatMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
public class ChatMessageRepository_Test {
    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Test
    void 메시지_저장(){
        // given
        ChatMessage chatMessage = ChatMessage.builder()
                .message("message")
                .localDateTime(LocalDateTime.now())
                .sender("sender")
                .build();

        // when
        Assertions.assertDoesNotThrow(()->{
            chatMessageRepository.save("classroomCode", chatMessage);
        });
    }

    @Test
    void 해당_수업의_대화_메시지_가져오기(){
        // given
        ChatMessage chatMessage = ChatMessage.builder()
                .message("message")
                .localDateTime(LocalDateTime.now())
                .sender("sender")
                .build();
        chatMessageRepository.save("classroomCode", chatMessage);

        // when
        List<ChatMessage> messages = chatMessageRepository.findAll("classroomCode");

        // then
        assertNotEquals(messages.size(), 0);
    }
}
