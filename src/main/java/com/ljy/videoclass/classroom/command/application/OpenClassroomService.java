package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.Register;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OpenClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ClassroomMapper classroomMapper;

    public OpenClassroomService(ClassroomRepository classroomRepository, ClassroomMapper classroomMapper) {
        this.classroomRepository = classroomRepository;
        this.classroomMapper = classroomMapper;
    }

    public void open(OpenClassroom openClassroom, Register register) {
        Classroom classroom = classroomMapper.mapfrom(openClassroom, register);
        classroomRepository.save(classroom);
    }
}
