package com.ljy.videoclass.classroom.command.application.event;

import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Requester;

public class ElrolmentedEvent {
    private final String code;
    private final String requester;
    public ElrolmentedEvent(ClassroomCode code, Requester requester) {
        this.code = code.get();
        this.requester = requester.get();
    }

    public String getCode() {
        return code;
    }

    public String getRequester() {
        return requester;
    }
}
