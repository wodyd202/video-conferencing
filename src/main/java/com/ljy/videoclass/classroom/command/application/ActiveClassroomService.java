package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ljy.videoclass.classroom.command.application.ClassroomServiceHelper.findByCodeAndRegister;

@Service
@Transactional
public class ActiveClassroomService {
    private final ClassroomRepository classroomRepository;

    public ActiveClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public void active(ClassroomCode classroomCode, Register register) {
        Classroom classroom = findByCodeAndRegister(classroomRepository, classroomCode, register);
        classroom.active();
        classroomRepository.save(classroom);
    }
}
