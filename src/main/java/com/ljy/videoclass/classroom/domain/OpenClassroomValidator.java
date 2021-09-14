package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.domain.ClassDateInfo;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.Register;

import java.util.List;

public interface OpenClassroomValidator {
    void validation(Register register, ClassDateInfo classDateInfo);
}
