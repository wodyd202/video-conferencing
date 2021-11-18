package com.ljy.videoclass.services.classroom.command.infrastructure;

import com.ljy.videoclass.services.classroom.command.application.ChatMessageRepository;
import com.ljy.videoclass.services.classroom.command.infrastructure.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Repository
public class RedisMessageChatMessageRepository implements ChatMessageRepository {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${redis.chat-message.key}")
    private String CHAT_MESSAGE_KEY;

    private ListOperations<String, Object> listOperations;

    @PostConstruct
    void setUp(){
        listOperations = redisTemplate.opsForList();
    }

    @Override
    public void save(String classroomCode, ChatMessage chatMessage) {
        listOperations.rightPush(CHAT_MESSAGE_KEY + ":" + classroomCode, chatMessage);
        log.info("save chat message into redis : {}", chatMessage);
    }

    @Override
    public List<ChatMessage> findAll(String classroomCode) {
        return listOperations.range(CHAT_MESSAGE_KEY + ":" + classroomCode, 0, 50).stream().map(chatMessage->(ChatMessage)chatMessage).collect(toList());
    }

}
