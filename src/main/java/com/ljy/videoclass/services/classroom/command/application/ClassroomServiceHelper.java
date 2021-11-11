package com.ljy.videoclass.services.classroom.command.application;

import com.ljy.videoclass.services.classroom.domain.Classroom;
import com.ljy.videoclass.services.classroom.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.services.classroom.domain.value.Register;

public class ClassroomServiceHelper {
    public static Classroom findByCodeAndRegister(ClassroomRepository classroomRepository, ClassroomCode classroomCode, Register register) {
        return classroomRepository.findByCodeAndRegister(classroomCode,register).orElseThrow(ClassroomNotFoundException::new);
    }
}
