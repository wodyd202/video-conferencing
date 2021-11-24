package com.ljy.videoclass.services.conference.domain.model;

import com.ljy.videoclass.services.conference.domain.event.OpenedConferenceEvent;
import com.ljy.videoclass.services.conference.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conference.domain.value.ConferenceTitle;
import com.ljy.videoclass.services.conference.domain.value.Creator;
import com.ljy.videoclass.services.conference.domain.value.LimitCount;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ConferenceModel {
    private String code;
    private String title;
    private int limitCount;
    private String creator;
    private LocalDateTime createDateTime;

    @Builder
    public ConferenceModel(ConferenceCode code,
                           ConferenceTitle title,
                           LimitCount limitCount,
                           Creator creator,
                           LocalDateTime createDateTime) {
        this.code = code.get();
        this.title = title.get();
        this.limitCount = limitCount.get();
        this.creator = creator.get();
        this.createDateTime = createDateTime;
    }

    public ConferenceModel(OpenedConferenceEvent event) {
        code = event.getCode();
        title = event.getTitle();
        limitCount = event.getLimitCount();
        creator = event.getCreator();
        createDateTime = event.getCreateDateTime();
    }

    @Override
    public String toString() {
        return "ConferenceModel{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", limitCount=" + limitCount +
                ", creator='" + creator + '\'' +
                ", createDateTime=" + createDateTime +
                '}';
    }
}
