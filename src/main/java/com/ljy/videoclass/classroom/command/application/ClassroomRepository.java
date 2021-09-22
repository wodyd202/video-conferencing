package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository {
    void save(Classroom classroom);

    Optional<Classroom> findByCodeAndRegister(ClassroomCode classroomCode, Register register);

    Optional<Classroom> findByCode(ClassroomCode classroomCode);
}
