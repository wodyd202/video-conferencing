package com.ljy.videoclass.services.conference.domain.event;

import com.ljy.videoclass.services.conference.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conference.domain.value.ConferenceTitle;
import com.ljy.videoclass.services.conference.domain.value.Creator;
import com.ljy.videoclass.services.conference.domain.value.LimitCount;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OpenedConferenceEvent {
    private String code;
    private String title;
    private int limitCount;
    private String creator;
    private LocalDateTime createDateTime;

    public OpenedConferenceEvent(ConferenceCode code, ConferenceTitle title, LimitCount limitCount, Creator creator, LocalDateTime createDateTime) {
        this.code = code.get();
        this.title = title.get();
        this.limitCount = limitCount.get();
        this.creator = creator.get();
        this.createDateTime = createDateTime;
    }
}
