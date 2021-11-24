package com.ljy.videoclass.services.panelist.domain.event;

import com.ljy.videoclass.services.panelist.domain.value.PanelistId;
import com.ljy.videoclass.services.panelist.domain.value.ExpellCount;
import com.ljy.videoclass.services.panelist.domain.value.PanelistStatus;
import com.ljy.videoclass.services.panelist.domain.value.Password;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpedPanelistEvent {
    private String id;
    private String password;
    private short expellCount;
    private PanelistStatus status;

    @Builder
    public SignUpedPanelistEvent(PanelistId id,
                                 Password password,
                                 ExpellCount expellCount,
                                 PanelistStatus status) {
        this.id = id.get();
        this.password = password.get();
        this.expellCount = expellCount.get();
        this.status = status;
    }
}
