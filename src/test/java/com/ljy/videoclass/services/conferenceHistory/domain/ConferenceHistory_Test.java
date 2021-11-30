package com.ljy.videoclass.services.conferenceHistory.domain;

import com.ljy.videoclass.services.conferenceHistory.application.ConferenceHistoryMapper;
import com.ljy.videoclass.services.conferenceHistory.domain.model.ConferenceHistoryModel;
import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conferenceHistory.domain.value.HistoryType;
import com.ljy.videoclass.services.conferenceHistory.domain.value.Payload;
import com.ljy.videoclass.services.rtcConference.application.event.ClosedConferenceEvent;
import com.ljy.videoclass.services.rtcConference.application.event.OpenedConferenceEvent;
import com.ljy.videoclass.services.rtcConference.model.PanelistId;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * 회의 이력 테스트
 */
public class ConferenceHistory_Test {

    @Test
    void 회의_생성_이력(){
        // given
        ConferenceHistory conferenceHistory = ConferenceHistory.builder()
                .code(ConferenceCode.of("code"))
                .type(HistoryType.OPEN)
                .payload(Payload.of("who?"))
                .build();

        // when
        ConferenceHistoryModel conferenceHistoryModel = conferenceHistory.toModel();

        // then
        assertEquals(conferenceHistoryModel.getCode(), "code");
        assertEquals(conferenceHistoryModel.getType(), HistoryType.OPEN);
        assertEquals(conferenceHistoryModel.getPayload(), "who?");
    }

    ConferenceHistoryMapper mapper = new ConferenceHistoryMapper();

    @Test
    void openedConferenceEventMapFrom(){
        // given
        com.ljy.videoclass.services.rtcConference.model.ConferenceCode conferenceCode = com.ljy.videoclass.services.rtcConference.model.ConferenceCode.of("conferenceCode");
        PanelistId panelistId = PanelistId.of("panelistId");
        OpenedConferenceEvent openedConferenceEvent = new OpenedConferenceEvent(conferenceCode, panelistId);

        // when
        ConferenceHistory conferenceHistory = mapper.mapFrom(openedConferenceEvent).get(0);
        ConferenceHistoryModel conferenceHistoryModel = conferenceHistory.toModel();

        // then
        assertEquals(conferenceHistoryModel.getCode(), "conferenceCode");
        assertEquals(conferenceHistoryModel.getType(), HistoryType.OPEN);
        assertEquals(conferenceHistoryModel.getPayload(), "panelistId");
    }

    @Test
    void ClosedConferenceEventMapFrom(){
        // given
        com.ljy.videoclass.services.rtcConference.model.ConferenceCode conferenceCode = com.ljy.videoclass.services.rtcConference.model.ConferenceCode.of("conferenceCode");
        ClosedConferenceEvent closedConferenceEvent = new ClosedConferenceEvent(conferenceCode);

        // when
        ConferenceHistory conferenceHistory = mapper.mapFrom(closedConferenceEvent).get(0);
        ConferenceHistoryModel conferenceHistoryModel = conferenceHistory.toModel();

        // then
        assertEquals(conferenceHistoryModel.getCode(), "conferenceCode");
        assertEquals(conferenceHistoryModel.getType(), HistoryType.CLOSE);
        assertNull(conferenceHistoryModel.getPayload());
    }

}
