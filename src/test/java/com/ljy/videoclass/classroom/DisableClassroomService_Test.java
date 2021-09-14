package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.command.application.DisableClassroomService;
import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;

@SpringBootTest
public class DisableClassroomService_Test {
    @Autowired OpenClassroomService openClassroomService;
    @Autowired DisableClassroomService disableClassroomService;

    @Test
    void disable(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));
        disableClassroomService.disable(ClassroomCode.of(classroomModel.getCode()), Register.of("00000000"));
    }
}
