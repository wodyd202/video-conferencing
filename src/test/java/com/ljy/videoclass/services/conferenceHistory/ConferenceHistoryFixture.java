package com.ljy.videoclass.services.conferenceHistory;

import com.ljy.videoclass.services.conferenceHistory.domain.ConferenceHistory;
import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conferenceHistory.domain.value.HistoryType;
import com.ljy.videoclass.services.conferenceHistory.domain.value.Payload;

public class ConferenceHistoryFixture {
    public static ConferenceHistory.ConferenceHistoryBuilder aConferenceHistory() {
        return ConferenceHistory.builder()
                .code(ConferenceCode.of("code"))
                .type(HistoryType.OPEN)
                .payload(Payload.of("payload"));
    }
}
