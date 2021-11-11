package com.ljy.videoclass.services.classroom.command.application.util;

import com.ljy.videoclass.services.classroom.command.application.ClassroomRepository;
import com.ljy.videoclass.services.classroom.domain.value.ClassDateInfo;
import com.ljy.videoclass.services.classroom.domain.OpenClassroomValidator;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import org.springframework.stereotype.Component;

@Component
public class SimpleOpenClassroomValidator implements OpenClassroomValidator {
    private final ClassroomRepository classroomRepository;

    public SimpleOpenClassroomValidator(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    /**
     * @param register
     * @param classDateInfo
     * - 강의 등록 제약
     */
    @Override
    public void validation(Register register, ClassDateInfo classDateInfo) {
    }
}
