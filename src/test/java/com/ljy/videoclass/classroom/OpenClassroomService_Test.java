package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.ChangeClassInfo;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.Register;
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
