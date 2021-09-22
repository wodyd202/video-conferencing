package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.command.application.event.AllowedElorlmentEvent;
import com.ljy.videoclass.classroom.domain.AllowElrolmentValidator;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.classroom.domain.value.Requester;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ljy.videoclass.classroom.command.application.ClassroomServiceHelper.findByCodeAndRegister;

@Service
@Transactional
public class AllowElrolmentService {
    private final ClassroomRepository classroomRepository;
    private final AllowElrolmentValidator allowElrolmentValidator;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AllowElrolmentService(ClassroomRepository classroomRepository, AllowElrolmentValidator allowElrolmentValidator, ApplicationEventPublisher applicationEventPublisher) {
        this.classroomRepository = classroomRepository;
        this.allowElrolmentValidator = allowElrolmentValidator;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void allowElrolment(ClassroomCode classroomCode, Requester requester, Register register) {
        Classroom classroom = findByCodeAndRegister(classroomRepository, classroomCode, register);
        classroom.allowedElrolment(allowElrolmentValidator, requester);
        applicationEventPublisher.publishEvent(new AllowedElorlmentEvent(classroomCode, requester));
    }
}
