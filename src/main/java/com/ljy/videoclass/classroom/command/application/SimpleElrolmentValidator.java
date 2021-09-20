package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.domain.ElrolmentValidator;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.springframework.stereotype.Component;

@Component
public class SimpleElrolmentValidator implements ElrolmentValidator {

    @Override
    public void validation(ClassroomCode classroomCode, Register requester) {

    }
}
