package com.ljy.videoclass.classroom.domain;

import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;

public interface ElrolmentValidator {
    void validation(ClassroomCode classroomCode, Register requester);
}
