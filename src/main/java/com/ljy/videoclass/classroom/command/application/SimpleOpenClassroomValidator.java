package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.command.application.ClassroomRepository;
import com.ljy.videoclass.classroom.domain.ClassDateInfo;
import com.ljy.videoclass.classroom.domain.Register;
import org.springframework.stereotype.Component;


@Component
public class SimpleOpenClassroomValidator implements OpenClassroomValidator {
    private final ClassroomRepository classroomRepository;

    public SimpleOpenClassroomValidator(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    @Override
    public void validation(Register register, ClassDateInfo classDateInfo) {

    }
}
