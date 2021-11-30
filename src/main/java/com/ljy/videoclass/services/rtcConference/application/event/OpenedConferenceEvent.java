package com.ljy.videoclass.services.rtcConference.application.event;

import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
import com.ljy.videoclass.services.rtcConference.model.PanelistId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OpenedConferenceEvent extends ConferenceEvent {
    private String creatorId;
    public OpenedConferenceEvent(ConferenceCode conferenceCode, PanelistId panelistId) {
        super(conferenceCode.get());
        this.creatorId = panelistId.get();
    }
}
