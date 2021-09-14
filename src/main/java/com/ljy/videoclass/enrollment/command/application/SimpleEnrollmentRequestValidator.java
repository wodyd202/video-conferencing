package com.ljy.videoclass.enrollment.command.application;

import com.ljy.videoclass.enrollment.domain.ClassroomModel;
import com.ljy.videoclass.enrollment.domain.ClassroomRepository;
import com.ljy.videoclass.enrollment.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.enrollment.domain.exception.NotAllowedEnrollmentException;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.EnrollmentRequsetValidator;
import com.ljy.videoclass.enrollment.domain.value.Requester;
import com.ljy.videoclass.enrollment.domain.exception.AlreadyErollmentException;
import org.springframework.stereotype.Component;

@Component
public class SimpleEnrollmentRequestValidator implements EnrollmentRequsetValidator {
    private final ClassroomRepository classroomRepository;
    private final EnrollmentRepository enrollmentRepository;

    public SimpleEnrollmentRequestValidator(ClassroomRepository classroomRepository, EnrollmentRepository enrollmentRepository) {
        this.classroomRepository = classroomRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public void validation(Requester requester, ClassroomCode classroomCode) {
        ClassroomModel classroomModel = classroomRepository.findByCode(classroomCode.get()).orElseThrow(ClassroomNotFoundException::new);
        if(requester.equals(Requester.of(classroomModel.getRegister()))){
            throw new NotAllowedEnrollmentException("자신의 강의에 수강신청할 수 없습니다.");
        }
        if(enrollmentRepository.findByRequesterAndCode(requester, classroomCode).isPresent()){
            throw new AlreadyErollmentException();
        }
    }
}
