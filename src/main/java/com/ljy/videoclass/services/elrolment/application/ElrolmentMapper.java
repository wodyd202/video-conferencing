package com.ljy.videoclass.services.elrolment.application;

import com.ljy.videoclass.services.classroom.domain.Classroom;
import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.RequesterInfo;
import org.springframework.stereotype.Component;

@Component
public class ElrolmentMapper {

    public Elrolment mapFrom(String classCode, RequesterInfo requesterInfo) {
        return Elrolment.builder()
                .code(ClassroomCode.of(classCode))
                .requesterInfo(requesterInfo)
                .build();
    }
}
