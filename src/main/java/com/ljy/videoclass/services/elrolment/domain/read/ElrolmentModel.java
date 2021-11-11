package com.ljy.videoclass.services.elrolment.domain.read;

import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.services.elrolment.domain.value.RequesterInfo;
import lombok.Builder;

import java.time.LocalDate;

public class ElrolmentModel {
    private String classroomCode;
    private ElrolmentRequesterInfoModel elrolmentRequesterInfo;
    private ElrolmentState state;
    private LocalDate elrolmentDate;

    @Builder
    public ElrolmentModel(ClassroomCode classroomCode,
                          RequesterInfo requester,
                          ElrolmentState state,
                          LocalDate elrolmentDate) {
        this.classroomCode = classroomCode.get();
        this.elrolmentRequesterInfo = requester.toModel();
        this.state = state;
        this.elrolmentDate = elrolmentDate;
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public ElrolmentRequesterInfoModel getRequester() {
        return elrolmentRequesterInfo;
    }

    public ElrolmentState getState() {
        return state;
    }

    public LocalDate getElrolmentDate() {
        return elrolmentDate;
    }
}
