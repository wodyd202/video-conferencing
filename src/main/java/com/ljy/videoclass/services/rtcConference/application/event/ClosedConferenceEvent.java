package com.ljy.videoclass.services.rtcConference.application.event;

import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ClosedConferenceEvent {
    private String conferenceCode;
    private LocalDateTime closeDateTime;
    public ClosedConferenceEvent(ConferenceCode conferenceCode) {
        this.conferenceCode = conferenceCode.get();
        closeDateTime = LocalDateTime.now();
    }
}
