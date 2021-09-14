package com.ljy.videoclass.classroom.query.application;

import com.ljy.videoclass.classroom.domain.read.ClassroomModel;

import java.util.List;

public interface ClassroomSearchRepository {
    List<ClassroomModel> findByRegister(String register);
}
