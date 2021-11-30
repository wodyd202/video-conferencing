package com.ljy.videoclass.services.conferenceHistory.application;

import com.ljy.videoclass.services.rtcConference.application.event.OpenedConferenceEvent;
import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
import com.ljy.videoclass.services.rtcConference.model.PanelistId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
public class ConferenceHistoryEventListener_Test {
    @Autowired ApplicationEventPublisher applicationEventPublisher;

    @Test
    void openedConferenceEvent(){
        // given
        ConferenceCode conferenceCode = ConferenceCode.of("conferenceCode");
        PanelistId panelistId = PanelistId.of("panelistId");
        OpenedConferenceEvent openedConferenceEvent = new OpenedConferenceEvent(conferenceCode, panelistId);

        // when
        applicationEventPublisher.publishEvent(openedConferenceEvent);
    }
}
