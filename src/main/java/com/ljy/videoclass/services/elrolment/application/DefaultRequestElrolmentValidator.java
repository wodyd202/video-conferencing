package com.ljy.videoclass.services.elrolment.application;

import com.ljy.videoclass.services.elrolment.command.application.ElrolmentRepository;
import com.ljy.videoclass.services.elrolment.domain.RequestElrolmentValidator;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.RequesterInfo;
import com.ljy.videoclass.services.elrolment.query.application.ClassroomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DefaultRequestElrolmentValidator implements RequestElrolmentValidator {
    private ClassroomRepository classroomRepository;
    private ElrolmentRepository elrolmentRepository;

    @Override
    public void validation(ClassroomCode classroomCode, RequesterInfo requesterInfo) {

    }
}
