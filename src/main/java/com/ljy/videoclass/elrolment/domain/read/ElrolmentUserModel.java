package com.ljy.videoclass.elrolment.domain.read;

import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.elrolment.domain.value.Requester;
import lombok.Builder;

import java.time.LocalDate;

public class ElrolmentUserModel {
    private String classroomCode;
    private String requester;
    private ElrolmentState state;
    private LocalDate elrolmentDate;

    @Builder
    private ElrolmentUserModel(ClassroomCode classroomCode,
                               Requester requester,
                               ElrolmentState state,
                               LocalDate elrolmentDate) {
        this.classroomCode = classroomCode.get();
        this.requester = requester.get();
        this.state = state;
        this.elrolmentDate = elrolmentDate;
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public String getRequester() {
        return requester;
    }

    public ElrolmentState getState() {
        return state;
    }

    public LocalDate getElrolmentDate() {
        return elrolmentDate;
    }
}
