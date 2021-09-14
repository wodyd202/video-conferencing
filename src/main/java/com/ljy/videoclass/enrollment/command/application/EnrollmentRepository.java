package com.ljy.videoclass.enrollment.command.application;

import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.Enrollment;
import com.ljy.videoclass.enrollment.domain.value.Requester;

import java.util.Optional;

public interface EnrollmentRepository {
    Optional<Enrollment> findByRequesterAndCode(Requester requester, ClassroomCode classroomCode);

    void save(Enrollment enrollment);
}
