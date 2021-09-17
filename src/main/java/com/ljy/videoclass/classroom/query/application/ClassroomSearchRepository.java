package com.ljy.videoclass.classroom.query.application;

import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomState;
import com.ljy.videoclass.classroom.query.model.ClassroomSearchModel;
import com.ljy.videoclass.core.http.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ClassroomSearchRepository {
    List<ClassroomModel> findByRegister(ClassroomState state, String register, PageRequest pageRequest);

    long countByRegister(ClassroomState state, String register);

    List<ClassroomModel> findByClassDateAndDayOfWeek(ClassroomSearchModel classroomSearchModel,PageRequest pageRequest, String register);

    long countByClassDateAndDayOfWeek(ClassroomSearchModel classroomSearchModel, String register);

    Optional<ClassroomModel> findbyCodeAndRegister(String classroomCode, String register);
}
