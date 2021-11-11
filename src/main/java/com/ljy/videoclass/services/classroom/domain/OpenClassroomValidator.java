package com.ljy.videoclass.services.classroom.domain;

import com.ljy.videoclass.services.classroom.domain.value.ClassDateInfo;
import com.ljy.videoclass.services.classroom.domain.value.Register;

public interface OpenClassroomValidator {
    void validation(Register register, ClassDateInfo classDateInfo);
}
