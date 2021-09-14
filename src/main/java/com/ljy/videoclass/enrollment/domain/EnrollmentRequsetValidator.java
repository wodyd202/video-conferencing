package com.ljy.videoclass.enrollment.domain;

import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.Requester;

public interface EnrollmentRequsetValidator {
    void validation(Requester requester, ClassroomCode classroomCode);
}
