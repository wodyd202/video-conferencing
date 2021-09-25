package com.ljy.videoclass.classroom.domain.read;

import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.ElrolmentState;

import java.time.LocalDate;

public class ElrolmentModel {
    private String code;
    private String requester;
    private String state;
    private LocalDate elrolmentDate;

    public ElrolmentModel(ClassroomCode classroomCode, String requester, ElrolmentState elrolmentState, LocalDate elrolmentDate){
        this.code = classroomCode.get();
        this.requester = requester;
        this.state = elrolmentState.toString();
        this.elrolmentDate = elrolmentDate;
    }

    public String getCode() {
        return code;
    }

    public String getRequester() {
        return requester;
    }

    public String getState() {
        return state;
    }

    public LocalDate getElrolmentDate() {
        return elrolmentDate;
    }
}
