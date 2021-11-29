package com.ljy.videoclass.services.conferenceHistory.command.application.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.services.panelist.command.application.model.Panelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Profile("!test")
@Repository
@Transactional(readOnly = true)
public class RedisExternalPanelistRepository implements ExternalPanelistRepository {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired private ObjectMapper objectMapper;
    private HashOperations<String, Object, Object> hashOperations;

    @Value("${redis.key.panelist}")
    private String PANELIST_KEY;

    @PostConstruct
    void setUp(){
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Optional<Panelist> getPanelist(String id) {
        Object obj = hashOperations.get(PANELIST_KEY, id);
        if(obj != null){
            return Optional.of(objectMapper.convertValue(obj, Panelist.class));
        }
        return Optional.empty();
    }
}
