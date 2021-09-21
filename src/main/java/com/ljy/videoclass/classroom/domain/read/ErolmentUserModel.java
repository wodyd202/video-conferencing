package com.ljy.videoclass.classroom.domain.read;

import com.ljy.videoclass.elrolment.domain.value.ElrolmentState;
import lombok.Builder;

import java.time.LocalDate;

public class ErolmentUserModel {
    private String userId;
    private ElrolmentState state;
    private LocalDate erolmentDate;

    @Builder
    private ErolmentUserModel(String userId, ElrolmentState state, LocalDate erolmentDate) {
        this.userId = userId;
        this.state = state;
        this.erolmentDate = erolmentDate;
    }

    public String getUserId() {
        return userId;
    }

    public ElrolmentState getState() {
        return state;
    }

    public LocalDate getErolmentDate() {
        return erolmentDate;
    }
}
