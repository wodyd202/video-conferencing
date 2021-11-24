package com.ljy.videoclass.services.conference.query.application;

import com.ljy.videoclass.services.conference.domain.event.OpenedConferenceEvent;
import com.ljy.videoclass.services.conference.domain.model.ConferenceModel;
import com.ljy.videoclass.services.conference.query.infrastructure.RedisConferenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 회의 이벤트 리스너
 */
@Slf4j
@Component
@Async("queryConferenceExecutor")
public class QueryConferenceEventListener {
    @Autowired private RedisConferenceRepository redisConferenceRepository;

    @EventListener
    protected void handle(OpenedConferenceEvent event){
        ConferenceModel conferenceModel = new ConferenceModel(event);
        redisConferenceRepository.save(conferenceModel);
        log.info("save conference into redis : {}", conferenceModel);
    }
}
