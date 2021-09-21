package com.ljy.videoclass.elrolment;

import com.ljy.videoclass.classroom.command.application.ElrolmentService;
import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.classroom.domain.value.Requester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class ElrolmentService_Test {
    @Autowired
    private OpenClassroomService openClassroomService;

    @Autowired
    private ElrolmentService elrolmentService;

    @Test
    @DisplayName("수강 신청")
    void elrolment(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        assertDoesNotThrow(()->{
            elrolmentService.elrolment(ClassroomCode.of(classroomModel.getCode()), Requester.of("elrolment"));
        });
    }
}
