package com.ljy.videoclass.elrolment.domain;

import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.ErolmentState;
import com.ljy.videoclass.elrolment.domain.value.Requester;
import lombok.Builder;

import java.time.LocalDate;

public class ElrolmentUser {
    private final ClassroomCode code;
    private final Requester userId;
    private ErolmentState state;
    private LocalDate erolmentDate;

    @Builder
    protected ElrolmentUser(ClassroomCode code, Requester userId) {
        this.code = code;
        this.userId = userId;
        state = ErolmentState.NOT;
        erolmentDate = LocalDate.now();
    }
}
