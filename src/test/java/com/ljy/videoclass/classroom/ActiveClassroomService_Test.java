package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.command.application.ActiveClassroomService;
import com.ljy.videoclass.classroom.command.application.DisableClassroomService;
import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;

@SpringBootTest
public class ActiveClassroomService_Test {
    @Autowired OpenClassroomService openClassroomService;
    @Autowired DisableClassroomService disableClassroomService;
    @Autowired ActiveClassroomService activeClassroomService;

    @Test
    void active(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Register register = Register.of("00000000");
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, register);
        disableClassroomService.disable(ClassroomCode.of(classroomModel.getCode()), register);

        activeClassroomService.active(ClassroomCode.of(classroomModel.getCode()), register);
    }
}
