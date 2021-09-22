package com.ljy.videoclass.elrolment.query.application;

import com.ljy.videoclass.elrolment.query.application.model.ClassroomModel;

import java.util.Optional;

public interface ClassroomRepository {
    Optional<ClassroomModel> findByCode(String classroomCode);
}
