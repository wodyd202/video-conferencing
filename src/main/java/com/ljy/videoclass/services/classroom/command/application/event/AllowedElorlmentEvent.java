package com.ljy.videoclass.services.classroom.command.application.event;

import com.ljy.videoclass.services.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.services.classroom.domain.value.Requester;

public class AllowedElorlmentEvent {
    private final String classroomCode;
    private final String requester;

    public AllowedElorlmentEvent(ClassroomCode classroomCode, Requester requester) {
        this.classroomCode = classroomCode.get();
        this.requester = requester.get();
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public String getRequester() {
        return requester;
    }
}
