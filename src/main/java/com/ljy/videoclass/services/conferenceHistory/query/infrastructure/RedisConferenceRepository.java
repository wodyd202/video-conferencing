package com.ljy.videoclass.services.conferenceHistory.query.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.services.conferenceHistory.domain.model.ConferenceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Repository
public class RedisConferenceRepository {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired private ObjectMapper objectMapper;
    private HashOperations<String, Object, Object> hashOperations;

    @Value("${redis.key.conference}")
    private String CONFERENCE_KEY;

    @PostConstruct
    void setUp(){
        hashOperations = redisTemplate.opsForHash();
    }

    public void save(ConferenceModel conferenceModel) {
        hashOperations.put(CONFERENCE_KEY, conferenceModel.getCode(), conferenceModel);
    }

    public Optional<ConferenceModel> findByCode(String code) {
        Object obj = hashOperations.get(CONFERENCE_KEY, code);
        if(obj != null){
            return Optional.of(objectMapper.convertValue(obj, ConferenceModel.class));
        }
        return Optional.empty();
    }
}
