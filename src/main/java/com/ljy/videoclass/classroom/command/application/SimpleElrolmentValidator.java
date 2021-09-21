package com.ljy.videoclass.classroom.command.application;

import com.ljy.videoclass.classroom.domain.ElrolmentValidator;
import com.ljy.videoclass.classroom.domain.exception.AlreadyElrolmentException;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Requester;
import org.springframework.stereotype.Component;

@Component
public class SimpleElrolmentValidator implements ElrolmentValidator {
    private final ElrolmentRepository elrolmentRepository;

    public SimpleElrolmentValidator(ElrolmentRepository elrolmentRepository) {
        this.elrolmentRepository = elrolmentRepository;
    }

    @Override
    public void validation(ClassroomCode classroomCode, Requester requester) {
        if(elrolmentRepository.findByClassroomCodeAndRequester(classroomCode.get(), requester.get()).isPresent()){
            throw new AlreadyElrolmentException();
        }
    }
}
