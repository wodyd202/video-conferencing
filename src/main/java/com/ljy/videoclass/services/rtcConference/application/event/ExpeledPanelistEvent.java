package com.ljy.videoclass.services.rtcConference.application.event;

import com.ljy.videoclass.services.rtcConference.model.PanelistId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class ExpeledPanelistEvent {
    private String targetId;
    private LocalDateTime expelDateTime;
    public ExpeledPanelistEvent(PanelistId target) {
        this.targetId = target.get();
        this.expelDateTime = LocalDateTime.now();
    }
}
