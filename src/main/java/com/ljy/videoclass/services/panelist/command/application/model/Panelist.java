package com.ljy.videoclass.services.panelist.command.application.model;

import com.ljy.videoclass.services.panelist.domain.value.PanelistStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static com.ljy.videoclass.services.panelist.domain.value.PanelistStatus.DE_ACTIVE;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Panelist {
    private String id;
    private PanelistStatus status;

    public boolean isNotActive() {
        return DE_ACTIVE.equals(status);
    }
}
