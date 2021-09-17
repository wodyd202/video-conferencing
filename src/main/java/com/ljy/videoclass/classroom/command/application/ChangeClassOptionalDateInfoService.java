package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.domain.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.OpenClassroomValidator;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ljy.videoclass.classroom.command.application.ClassroomServiceHelper.findByCodeAndRegister;

@Service
@Transactional
public class ChangeClassOptionalDateInfoService {
    private final ClassroomRepository classroomRepository;
    private final OpenClassroomValidator openClassroomValidator;

    public ChangeClassOptionalDateInfoService(ClassroomRepository classroomRepository, OpenClassroomValidator openClassroomValidator) {
        this.classroomRepository = classroomRepository;
        this.openClassroomValidator = openClassroomValidator;
    }

    public ClassroomModel changeClassOptionalDateInfo(ChangeClassOptionalDateInfo classOptionalDateInfo,
                                                      ClassroomCode classroomCode,
                                                      Register register) {
        Classroom classroom = findByCodeAndRegister(classroomRepository, classroomCode, register);
        classroom.changeClassOptionalDateInfo(classOptionalDateInfo);
        return classroom.toModel();
    }
}
