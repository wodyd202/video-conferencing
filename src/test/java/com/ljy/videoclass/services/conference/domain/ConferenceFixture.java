package com.ljy.videoclass.services.conference.domain;

import com.ljy.videoclass.services.conference.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conference.domain.value.ConferenceTitle;
import com.ljy.videoclass.services.conference.domain.value.LimitCount;

public class ConferenceFixture {
    public static Conference.ConferenceBuilder aConference() {
        return Conference.builder()
                .code(ConferenceCode.of("code"))
                .title(ConferenceTitle.of("회의실 제목"))
                .limitCount(LimitCount.of(2));
    }
}
