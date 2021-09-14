package com.ljy.videoclass.enrollment.domain.read;

import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.EnrollmentState;
import com.ljy.videoclass.enrollment.domain.value.Requester;
import lombok.Builder;

public class EnrollmentModel {
    private String requester;
    private String classroomCode;
    private EnrollmentState state;

    @Builder
    public EnrollmentModel(Requester requester, ClassroomCode classroomCode, EnrollmentState state) {
        this.requester = requester.get();
        this.classroomCode = classroomCode.get();
        this.state = state;
    }

    public String getRequester() {
        return requester;
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public EnrollmentState getState() {
        return state;
    }
}
