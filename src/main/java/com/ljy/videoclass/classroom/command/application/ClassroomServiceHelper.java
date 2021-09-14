package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;

public class ClassroomServiceHelper {

    public static Classroom findByCodeAndRegister(ClassroomRepository classroomRepository, ClassroomCode classroomCode, Register register) {
        return classroomRepository.findByCodeAndRegister(classroomCode,register).orElseThrow(ClassroomNotFoundException::new);
    }
}
