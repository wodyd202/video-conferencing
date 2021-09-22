package com.ljy.videoclass.classroom.domain;

import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.ClassroomState;
import com.ljy.videoclass.classroom.domain.value.Requester;

public interface AllowElrolmentValidator {
    void validation(ClassroomCode code, Requester requester, ClassroomState state);
}
