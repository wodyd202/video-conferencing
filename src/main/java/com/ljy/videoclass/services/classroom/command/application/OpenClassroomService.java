package com.ljy.videoclass.services.classroom.command.application;

import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.Classroom;
import com.ljy.videoclass.services.classroom.domain.OpenClassroomValidator;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 수업 개설 서비스
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class OpenClassroomService {
    private ClassroomMapper classroomMapper;
    private ClassroomRepository classroomRepository;
    private OpenClassroomValidator openClassroomValidator;

    public ClassroomModel open(OpenClassroom openClassroom, Register register) {
        // map
        Classroom classroom = classroomMapper.mapFrom(openClassroom, register);

        classroom.open(openClassroomValidator);

        classroomRepository.save(classroom);
        ClassroomModel classroomModel = classroom.toModel();
        log.info("save class into database : ", classroomModel);
        return classroomModel;
    }
}
