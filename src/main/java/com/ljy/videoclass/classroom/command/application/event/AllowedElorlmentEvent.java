package com.ljy.videoclass.classroom.command.application.event;

import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Requester;
import org.springframework.context.ApplicationEvent;

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
