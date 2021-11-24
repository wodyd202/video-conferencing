package com.ljy.videoclass.services.panelist.query.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.services.panelist.domain.model.PanelistModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Repository
public class RedisQueryPanelistRepository {
    @Autowired private RedisTemplate<String, Object> redisTemplate;
    @Autowired private ObjectMapper objectMapper;
    private HashOperations<String, Object, Object> hashOperations;

    @Value("${redis.key.panelist}")
    private String PANELIST_KEY;

    @PostConstruct
    void setUp(){
        hashOperations = redisTemplate.opsForHash();
    }

    public void save(PanelistModel panelistModel){
        hashOperations.put(PANELIST_KEY, panelistModel.getId(), panelistModel);
    }

    public Optional<PanelistModel> findById(String id){
        Object obj = hashOperations.get(PANELIST_KEY, id);
        if(obj != null){
            return Optional.of(objectMapper.convertValue(obj, PanelistModel.class));
        }
        return Optional.empty();
    }
}
