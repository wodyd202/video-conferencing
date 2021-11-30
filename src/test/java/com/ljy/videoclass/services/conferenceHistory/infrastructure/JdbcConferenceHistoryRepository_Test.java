package com.ljy.videoclass.services.conferenceHistory.infrastructure;

import com.ljy.videoclass.services.conferenceHistory.domain.ConferenceHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.ljy.videoclass.services.conferenceHistory.ConferenceHistoryFixture.aConferenceHistory;

@SpringBootTest
public class JdbcConferenceHistoryRepository_Test {
    @Autowired
    JdbcConferenceHistoryRepository jdbcConferenceHistoryRepository;

    @Test
    public void batchSave(){
        List<ConferenceHistory> conferenceHistories = Arrays.asList(aConferenceHistory().build(),aConferenceHistory().build(),aConferenceHistory().build());
        jdbcConferenceHistoryRepository.save(conferenceHistories);
    }
}
