package com.ljy.videoclass.elrolment.domain.read;

import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.elrolment.domain.value.Requester;
import com.ljy.videoclass.elrolment.domain.value.RequesterInfo;
import lombok.Builder;

import java.time.LocalDate;

public class ElrolmentUserModel {
    private String classroomCode;
    private ElrolmentRequesterInfoModel elrolmentRequesterInfo;
    private ElrolmentState state;
    private LocalDate elrolmentDate;

    @Builder
    public ElrolmentUserModel(ClassroomCode classroomCode,
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
