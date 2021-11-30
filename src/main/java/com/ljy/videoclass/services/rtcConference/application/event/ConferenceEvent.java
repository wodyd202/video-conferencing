package com.ljy.videoclass.services.rtcConference.application.event;

import lombok.Getter;

@Getter
abstract public class ConferenceEvent {
    private String conferenceCode;

    public ConferenceEvent(String conferenceCode) {
        this.conferenceCode = conferenceCode;
    }
}
