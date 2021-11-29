package com.ljy.videoclass.services.conferenceHistory.command.application;

import com.ljy.videoclass.services.conferenceHistory.command.model.OpenConference;
import com.ljy.videoclass.services.conferenceHistory.domain.Conference;
import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceTitle;
import com.ljy.videoclass.services.conferenceHistory.domain.value.LimitCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConferenceMapper {

    public Conference mapFrom(ConferenceCode conferenceCode, OpenConference openConference) {
        Conference conference = Conference.builder()
                .code(conferenceCode)
                .title(ConferenceTitle.of(openConference.getTitle()))
                .limitCount(LimitCount.of(openConference.getLimitCount()))
                .build();
        log.info("map from : {} to {}", openConference ,conference);
        return conference;
    }
}
