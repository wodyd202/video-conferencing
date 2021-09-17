package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.command.application.ChangeClassOptionalDateInfoService;
import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassOptionalDateInfoModel;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChangeClassOptionalDateInfo_Test {
    @Autowired
    OpenClassroomService openClassroomService;

    @Autowired
    ChangeClassOptionalDateInfoService changeClassOptionalDateInfoService;

    @Test
    void changeOptioanlDateInfo_notEmpty(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Register register = Register.of("00000000");
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, register);

        ChangeClassOptionalDateInfo classOptionalDateInfo = ChangeClassOptionalDateInfo.builder()
                .autoEnabled(false)
                .startDate(LocalDate.of(2020,1,1))
                .endDate(LocalDate.of(2020,12,25))
                .build();
        classroomModel = changeClassOptionalDateInfoService.changeClassOptionalDateInfo(classOptionalDateInfo, ClassroomCode.of(classroomModel.getCode()),register);

        ClassOptionalDateInfoModel classOptionalDateInfoModel = classroomModel.getClassOptionalDateInfo();
        assertEquals(classOptionalDateInfoModel.getStartDate(), LocalDate.of(2020,1,1));
        assertEquals(classOptionalDateInfoModel.getEndDate(), LocalDate.of(2020,12,25));
        assertFalse(classOptionalDateInfoModel.isAutoEnabled());
    }

    @Test
    void changeOptioanlDateInfo_empty(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Register register = Register.of("00000000");
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, register);

        classroomModel = changeClassOptionalDateInfoService.changeClassOptionalDateInfo(null, ClassroomCode.of(classroomModel.getCode()),register);

        ClassOptionalDateInfoModel classOptionalDateInfoModel = classroomModel.getClassOptionalDateInfo();
        assertNull(classOptionalDateInfoModel);
    }
}
