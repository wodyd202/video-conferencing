package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.OpenClassroomValidator;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OpenClassroomService {
    private final ClassroomRepository classroomRepository;
    private final OpenClassroomValidator openClassroomValidator;

    public OpenClassroomService(ClassroomRepository classroomRepository, OpenClassroomValidator openClassroomValidator) {
        this.classroomRepository = classroomRepository;
        this.openClassroomValidator = openClassroomValidator;
    }

    public ClassroomModel open(OpenClassroom openClassroom, Register register) {
        Classroom classroom = Classroom.createWith(openClassroom, register);
        classroom.open(openClassroomValidator);
        classroomRepository.save(classroom);
        return classroom.toModel();
    }
}
