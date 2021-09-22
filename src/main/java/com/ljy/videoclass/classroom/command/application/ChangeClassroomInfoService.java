package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.domain.ChangeClassInfo;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ljy.videoclass.classroom.command.application.ClassroomServiceHelper.findByCodeAndRegister;

/**
 * 수업 정보 변경 서비스
 */
@Service
@Transactional
public class ChangeClassroomInfoService {
    private final ClassroomRepository classroomRepository;

    public ChangeClassroomInfoService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public ClassroomModel changeClassInfo(ChangeClassInfo changeClassInfo, ClassroomCode classroomCode, Register register) {
        Classroom classroom = findByCodeAndRegister(classroomRepository, classroomCode, register);
        classroom.changeClassInfo(changeClassInfo);
        classroomRepository.save(classroom);
        return classroom.toModel();
    }
}
