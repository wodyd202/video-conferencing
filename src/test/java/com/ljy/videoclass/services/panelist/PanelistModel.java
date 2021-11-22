package com.ljy.videoclass.services.panelist;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PanelistModel {
    private String email;
    private boolean auth;
    private PanelistStatus status;
    private short expellCount;

    @Builder
    public PanelistModel(Email email, boolean auth, PanelistStatus status, ExpellCount expellCount) {
        this.email = email.get();
        this.auth = auth;
        this.status = status;
        this.expellCount = expellCount.get();
    }

    public short getExpellCount() {
        return expellCount;
    }

    public PanelistStatus getStatus() {
        return status;
    }
}
