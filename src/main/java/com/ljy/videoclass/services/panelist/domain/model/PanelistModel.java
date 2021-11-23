package com.ljy.videoclass.services.panelist.domain.model;

import com.ljy.videoclass.services.panelist.domain.event.SignUpedPanelist;
import com.ljy.videoclass.services.panelist.domain.value.Email;
import com.ljy.videoclass.services.panelist.domain.value.ExpellCount;
import com.ljy.videoclass.services.panelist.domain.value.PanelistStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PanelistModel {
    private String email;
    private String password;
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

    public PanelistModel(SignUpedPanelist event) {
        email = event.getEmail();
        password = event.getPassword();
        auth = false;
        status = PanelistStatus.ACTIVE;
        expellCount = 0;
    }

    @Override
    public String toString() {
        return "PanelistModel{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", auth=" + auth +
                ", status=" + status +
                ", expellCount=" + expellCount +
                '}';
    }
}
