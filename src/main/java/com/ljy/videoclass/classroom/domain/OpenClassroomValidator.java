package com.ljy.videoclass.classroom.domain;

import com.ljy.videoclass.classroom.domain.value.ClassDateInfo;
import com.ljy.videoclass.classroom.domain.value.Register;

public interface OpenClassroomValidator {
    void validation(Register register, ClassDateInfo classDateInfo);
}
