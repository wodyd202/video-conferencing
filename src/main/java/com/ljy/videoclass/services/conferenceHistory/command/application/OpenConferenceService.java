package com.ljy.videoclass.services.conferenceHistory.command.application;

import com.ljy.videoclass.services.conferenceHistory.command.model.OpenConference;
import com.ljy.videoclass.services.conferenceHistory.domain.Conference;
import com.ljy.videoclass.services.conferenceHistory.domain.ConferenceRepository;
import com.ljy.videoclass.services.conferenceHistory.domain.model.ConferenceModel;
import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conferenceHistory.domain.value.Creator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 회의 개최 서비스
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class OpenConferenceService {
    private SimpleOpenConferenceValidator openConferenceValidator;
    private ConferenceMapper conferenceMapper;
    private ConferenceRepository conferenceRepository;

    /**
     * @param creator
     * @param openConference
     * # 회의 개최
     */
    public ConferenceModel open(Creator creator, OpenConference openConference) {
        Conference conference = conferenceMapper.mapFrom(createCode(), openConference);
        conference.open(creator, openConferenceValidator);

        conferenceRepository.save(conference);
        ConferenceModel conferenceModel = conference.toModel();
        log.info("conference save success : {}", conferenceModel);
        return conferenceModel;
    }

    private ConferenceCode createCode() {
        return ConferenceCode.of(UUID.randomUUID().toString());
    }
}
