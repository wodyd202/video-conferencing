package com.ljy.videoclass.services.classroom.application;

import com.ljy.videoclass.services.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassInfo;
import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OpenClassroomService_Test {
    @Autowired
    OpenClassroomService openClassroomService;

    @Test
    @DisplayName("수업 개설")
    void open(){
        OpenClassroom openClassroom = OpenClassroom.builder()
                .classInfo(ChangeClassInfo.builder()
                        .title("수업명")
                        .build())
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(14)
                        .endHour(15)
                        .build())
                .build();

        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));
        assertNotNull(classroomModel);
    }
}
