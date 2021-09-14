package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.command.application.ChangeClassDateInfoService;
import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassDateInfoModel;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ChangeClassDateInfoService_Test {
    @Autowired OpenClassroomService openClassroomService;
    @Autowired ChangeClassDateInfoService changeClassDateInfoService;

    @Test
    void changeClassDateInfo(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Register register = Register.of("00000000");
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, register);

        classroomModel = changeClassDateInfoService.changeClassDateInfo(ChangeClassDateInfo.builder()
                        .endHour(17)
                        .startHour(16)
                        .dayOfWeek(DayOfWeek.SUNDAY)
                .build(), ClassroomCode.of(classroomModel.getCode()), register);
        ClassDateInfoModel classDateInfo = classroomModel.getClassDateInfo();
        assertEquals(classDateInfo.getStartHour(), 16);
        assertEquals(classDateInfo.getEndHour(), 17);
        assertEquals(classDateInfo.getDayOfWeek(), DayOfWeek.SUNDAY);
    }
}
