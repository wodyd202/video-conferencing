package com.ljy.videoclass.enrollment.command.application;

import com.ljy.videoclass.enrollment.domain.Enrollment;
import com.ljy.videoclass.enrollment.domain.EnrollmentRequsetValidator;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.Requester;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EnrollmentService {
    private final EnrollmentRequsetValidator enrollmentRequsetValidator;
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRequsetValidator enrollmentRequsetValidator, EnrollmentRepository enrollmentRepository) {
        this.enrollmentRequsetValidator = enrollmentRequsetValidator;
        this.enrollmentRepository = enrollmentRepository;
    }

    public void enrollment(ClassroomCode classroomCode, Requester requester) {
        Enrollment enrollment = Enrollment.createWith(classroomCode, requester);
        enrollment.requestEnrollment(enrollmentRequsetValidator);
        enrollmentRepository.save(enrollment);
    }
}
