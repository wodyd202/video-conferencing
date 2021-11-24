package com.ljy.videoclass.services.conference.query.application;

import com.ljy.videoclass.services.conference.domain.exception.ConferenceNotFoundException;
import com.ljy.videoclass.services.conference.domain.model.ConferenceModel;
import com.ljy.videoclass.services.conference.query.infrastructure.RedisConferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 회의 조회 서비스
 */
@Service
@AllArgsConstructor
public class ConferenceSearchService {
    private RedisConferenceRepository redisConferenceRepository;

    /**
     * @param code
     * # 회의 단건 조회
     */
    public ConferenceModel getConference(String code) {
        ConferenceModel conferenceModel = redisConferenceRepository.findByCode(code).orElseThrow(ConferenceNotFoundException::new);
        return conferenceModel;
    }
}
