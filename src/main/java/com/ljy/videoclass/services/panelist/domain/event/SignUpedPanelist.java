package com.ljy.videoclass.services.panelist.domain.event;

import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.value.Email;
import com.ljy.videoclass.services.panelist.domain.value.ExpellCount;
import com.ljy.videoclass.services.panelist.domain.value.PanelistStatus;
import com.ljy.videoclass.services.panelist.domain.value.Password;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpedPanelist {
    private String email;
    private String password;
    private boolean auth;
    private short expellCount;
    private PanelistStatus status;

    @Builder
    public SignUpedPanelist(Email email,
                            Password password,
                            boolean auth,
                            ExpellCount expellCount,
                            PanelistStatus status) {
        this.email = email.get();
        this.password = password.get();
        this.auth = auth;
        this.expellCount = expellCount.get();
        this.status = status;
    }
}
