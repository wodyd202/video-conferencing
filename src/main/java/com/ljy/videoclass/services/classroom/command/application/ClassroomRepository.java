package com.ljy.videoclass.services.classroom.command.application;

import com.ljy.videoclass.services.classroom.domain.Classroom;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.services.classroom.domain.value.Register;

import java.util.Optional;

public interface ClassroomRepository {
    void save(Classroom classroom);

    Optional<Classroom> findByCodeAndRegister(ClassroomCode classroomCode, Register register);

    Optional<Classroom> findByCode(ClassroomCode classroomCode);
}
