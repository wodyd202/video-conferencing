package com.ljy.videoclass.services.panelist.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ljy.videoclass.services.panelist.domain.event.SignUpedPanelistEvent;
import com.ljy.videoclass.services.panelist.domain.value.PanelistId;
import com.ljy.videoclass.services.panelist.domain.value.ExpellCount;
import com.ljy.videoclass.services.panelist.domain.value.PanelistStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PanelistModel {
    private String id;
    private String password;
    private PanelistStatus status;
    private int expellCount;

    @Builder
    public PanelistModel(PanelistId id, PanelistStatus status, ExpellCount expellCount) {
        this.id = id.get();
        this.status = status;
        this.expellCount = expellCount.get();
    }

    public PanelistModel(SignUpedPanelistEvent event) {
        id = event.getId();
        password = event.getPassword();
        status = PanelistStatus.ACTIVE;
        expellCount = 0;
    }

    public void increamentExpelCount() {
        expellCount++;
        if(expellCount == 5){
            expellCount = 0;
            status = PanelistStatus.DE_ACTIVE;
        }
    }

    public void emptyPassword(){
        password = null;
    }

    @Override
    public String toString() {
        return "PanelistModel{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", expellCount=" + expellCount +
                '}';
    }
}
