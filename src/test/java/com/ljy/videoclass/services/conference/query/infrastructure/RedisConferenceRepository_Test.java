package com.ljy.videoclass.services.conference.query.infrastructure;

import com.ljy.videoclass.services.conference.domain.Conference;
import com.ljy.videoclass.services.conference.domain.ConferenceFixture;
import com.ljy.videoclass.services.conference.domain.OpenConferenceValidator;
import com.ljy.videoclass.services.conference.domain.model.ConferenceModel;
import com.ljy.videoclass.services.conference.domain.value.Creator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ljy.videoclass.services.conference.domain.ConferenceFixture.aConference;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class RedisConferenceRepository_Test {
    @Autowired RedisConferenceRepository redisConferenceRepository;

    @Test
    void 레디스에_회의_정보_저장(){
        // given
        Conference conference = aConference().build();
        conference.open(Creator.of("생성자"), mock(OpenConferenceValidator.class));
        ConferenceModel conferenceModel = conference.toModel();

        // when
        redisConferenceRepository.save(conferenceModel);
        conferenceModel = redisConferenceRepository.findByCode(conferenceModel.getCode()).get();

        // then
        assertNotNull(conferenceModel);
    }
}
