package com.ljy.videoclass.services.rtcConference.infrastructure;

import com.ljy.videoclass.services.rtcConference.model.ChatMessage;
import com.ljy.videoclass.services.rtcConference.application.ChatMessageRepository;
import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
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

    @Value("${redis.key.chat-message}")
    private String CHAT_MESSAGE_KEY;

    private ListOperations<String, Object> listOperations;

    @PostConstruct
    void setUp(){
        listOperations = redisTemplate.opsForList();
    }

    @Override
    public void save(ConferenceCode conferenceCode, ChatMessage chatMessage) {
        listOperations.rightPush(CHAT_MESSAGE_KEY + ":" + conferenceCode.get(), chatMessage);
        log.info("save chat message into redis : {}", chatMessage);
    }

    @Override
    public List<ChatMessage> findAll(ConferenceCode conferenceCode, int start, int end) {
        return listOperations.range(CHAT_MESSAGE_KEY + ":" + conferenceCode.get(), start, end).stream().map(chatMessage->(ChatMessage)chatMessage).collect(toList());
    }

    @Override
    public long countAll(ConferenceCode conferenceCode) {
        return listOperations.size(CHAT_MESSAGE_KEY + ":" + conferenceCode.get());
    }

    @Override
    public List<ChatMessage> remove(ConferenceCode conferenceCode, int count) {
        List<ChatMessage> chatMessages = findAll(conferenceCode, 0, count);
        for(int i =0;i<count;i++){
            listOperations.leftPop(CHAT_MESSAGE_KEY + ":" + conferenceCode.get());
        }
        log.info("remove chatmessages into redis : {}", chatMessages);
        return chatMessages;
    }

    @Override
    public List<ChatMessage> remove(ConferenceCode conferenceCode) {
        List<ChatMessage> chatMessages = findAll(conferenceCode, 0, -1);
        redisTemplate.delete(CHAT_MESSAGE_KEY + ":" + conferenceCode.get());
        log.info("remove key : {}", CHAT_MESSAGE_KEY + ":" + conferenceCode.get());
        return chatMessages;
    }

}
