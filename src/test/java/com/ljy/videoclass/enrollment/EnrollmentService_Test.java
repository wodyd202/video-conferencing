package com.ljy.videoclass.enrollment;

import com.ljy.videoclass.classroom.ClassroomFixture;
import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.enrollment.command.application.EnrollmentService;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.Requester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EnrollmentService_Test {
    @Autowired OpenClassroomService openClassroomService;
    @Autowired EnrollmentService enrollmentService;

    @Test
    void enrollment(){
        OpenClassroom openClassroom = ClassroomFixture.aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000"));

        enrollmentService.enrollment(ClassroomCode.of(classroomModel.getCode()),Requester.of("000000"));
    }
}
