package com.ljy.videoclass.enrollment;

import com.ljy.videoclass.enrollment.domain.ClassroomModel;
import com.ljy.videoclass.enrollment.domain.ClassroomRepository;
import com.ljy.videoclass.enrollment.command.application.EnrollmentRepository;
import com.ljy.videoclass.enrollment.command.application.SimpleEnrollmentRequestValidator;
import com.ljy.videoclass.enrollment.domain.exception.AlreadyErollmentException;
import com.ljy.videoclass.enrollment.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.enrollment.domain.read.EnrollmentModel;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.Enrollment;
import com.ljy.videoclass.enrollment.domain.EnrollmentRequsetValidator;
import com.ljy.videoclass.enrollment.domain.value.EnrollmentState;
import com.ljy.videoclass.enrollment.domain.value.Requester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Enrollment_Test {

    ClassroomRepository classroomRepository;
    EnrollmentRepository enrollmentRepository;
    EnrollmentRequsetValidator enrollmentRequsetValidator;

    @BeforeEach
    void setUp() {
        enrollmentRepository = mock(EnrollmentRepository.class);
        classroomRepository = mock(ClassroomRepository.class);
        when(classroomRepository.findByCode("code"))
                .thenReturn(Optional.of(mock(ClassroomModel.class)));
        enrollmentRequsetValidator = new SimpleEnrollmentRequestValidator(classroomRepository, enrollmentRepository);
    }

    @Test
    @DisplayName("해당 강의가 존재하지 않음")
    void enrollment_notfound(){
        Enrollment enrollment = Enrollment.createWith(ClassroomCode.of("notfound"), Requester.of("00000000"));

        assertThrows(ClassroomNotFoundException.class,()->{
            enrollment.requestEnrollment(enrollmentRequsetValidator);
        });
    }

    @Test
    @DisplayName("이미 해당 강의를 수강신청한 상태")
    void enrollment_alreadyEnrollmented(){
        Enrollment enrollment = Enrollment.createWith(ClassroomCode.of("code"), Requester.of("00000000"));
        when(enrollmentRepository.findByRequesterAndCode(Requester.of("00000000"),ClassroomCode.of("code")))
                .thenReturn(Optional.of(mock(Enrollment.class)));

        assertThrows(AlreadyErollmentException.class,()->{
            enrollment.requestEnrollment(enrollmentRequsetValidator);
        });
    }

    @Test
    @DisplayName("수강신청")
    void enrollemnt(){
        Enrollment enrollment = Enrollment.createWith(ClassroomCode.of("code"), Requester.of("00000000"));
        enrollment.requestEnrollment(enrollmentRequsetValidator);
        EnrollmentModel enrollmentModel = enrollment.toModel();

        assertEquals(enrollmentModel.getRequester(), "00000000");
        assertEquals(enrollmentModel.getClassroomCode(), "code");
        assertEquals(enrollmentModel.getState(), EnrollmentState.Disapproval);
    }
}
