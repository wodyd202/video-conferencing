package com.ljy.videoclass.classroom.domain.read;

import com.ljy.videoclass.elrolment.domain.value.ErolmentState;
import lombok.Builder;

import java.time.LocalDate;

public class ErolmentUserModel {
    private String userId;
    private ErolmentState state;
    private LocalDate erolmentDate;

    @Builder
    private ErolmentUserModel(String userId, ErolmentState state, LocalDate erolmentDate) {
        this.userId = userId;
        this.state = state;
        this.erolmentDate = erolmentDate;
    }

    public String getUserId() {
        return userId;
    }

    public ErolmentState getState() {
        return state;
    }

    public LocalDate getErolmentDate() {
        return erolmentDate;
    }
}
