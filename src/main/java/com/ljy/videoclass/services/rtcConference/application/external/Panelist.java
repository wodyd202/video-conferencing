package com.ljy.videoclass.services.rtcConference.application.external;

import com.ljy.videoclass.services.panelist.domain.value.PanelistStatus;
import lombok.Getter;

@Getter
public class Panelist {
    private String id;
    private PanelistStatus status;

    public Panelist(String id, PanelistStatus status) {
        this.id = id;
        this.status = status;
    }

    public boolean isActive() {
        return status.equals(PanelistStatus.ACTIVE);
    }
}
