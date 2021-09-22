package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.command.application.event.ElrolmentedEvent;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.ElrolmentValidator;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Requester;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ljy.videoclass.classroom.command.application.ClassroomServiceHelper.findByCode;

/**
 * 수강신청 서비스
 */
@Service
@Transactional
public class ElrolmentService {
    private final ClassroomRepository classroomRepository;
    private final ElrolmentValidator elrolmentValidator;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ElrolmentService(ClassroomRepository classroomRepository, ElrolmentValidator elrolmentValidator, ApplicationEventPublisher applicationEventPublisher) {
        this.classroomRepository = classroomRepository;
        this.elrolmentValidator = elrolmentValidator;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void elrolment(ClassroomCode classroomCode, Requester requester) {
        Classroom classroom = findByCode(classroomRepository, classroomCode);
        classroom.elrolment(elrolmentValidator, requester);
        applicationEventPublisher.publishEvent(new ElrolmentedEvent(classroomCode, requester));
    }
}
