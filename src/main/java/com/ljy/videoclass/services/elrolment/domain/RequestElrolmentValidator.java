package com.ljy.videoclass.services.elrolment.domain;

import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.RequesterInfo;

public interface RequestElrolmentValidator {
    void validation(ClassroomCode classroomCode, RequesterInfo requesterInfo);
}
