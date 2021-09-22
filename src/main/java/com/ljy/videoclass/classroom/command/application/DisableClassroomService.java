package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.command.application.ClassroomRepository;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ljy.videoclass.classroom.command.application.ClassroomServiceHelper.findByCodeAndRegister;

/**
 * 수업 비활성화 서비스
 */
@Service
@Transactional
public class DisableClassroomService {
    private final ClassroomRepository classroomRepository;

    public DisableClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public void disable(ClassroomCode classroomCode, Register register) {
        Classroom classroom = findByCodeAndRegister(classroomRepository,classroomCode,register);
        classroom.disable();
        classroomRepository.save(classroom);
    }
}
