package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ljy.videoclass.classroom.command.application.ClassroomServiceHelper.findByCodeAndRegister;

@Service
@Transactional
public class ChangeClassDateInfoService {
    private final ClassroomRepository classroomRepository;

    public ChangeClassDateInfoService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public ClassroomModel changeClassDateInfo(ChangeClassDateInfo changeClassDateInfo, ClassroomCode classroomCode, Register register) {
        Classroom classroom = findByCodeAndRegister(classroomRepository, classroomCode, register);
        classroom.changeClassDateInfo(changeClassDateInfo);
        classroomRepository.save(classroom);
        return classroom.toModel();
    }
}
