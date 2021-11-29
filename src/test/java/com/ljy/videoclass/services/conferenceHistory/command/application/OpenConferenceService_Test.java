package com.ljy.videoclass.services.conferenceHistory.command.application;

import com.ljy.videoclass.services.conferenceHistory.command.model.OpenConference;
import com.ljy.videoclass.services.conferenceHistory.domain.model.ConferenceModel;
import com.ljy.videoclass.services.conferenceHistory.domain.value.Creator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
public class OpenConferenceService_Test {
    @Autowired
    OpenConferenceService openConferenceService;

    @Test
    void 회의_개최(){
        // given
        OpenConference openConference = OpenConference.builder()
                .title("회의실 제목")
                .limitCount(2)
                .build();

        // when
        ConferenceModel conferenceModel = openConferenceService.open(Creator.of("생성자"), openConference);

        // then
        assertNotNull(conferenceModel);
    }
}
