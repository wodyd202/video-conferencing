package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.command.application.ChangeClassroomInfoService;
import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.ChangeClassInfo;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Color;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ChangeClassroomInfoService_Test {
    @Autowired OpenClassroomService openClassroomService;
    @Autowired ChangeClassroomInfoService changeClassroomInfoService;

    @Test
    @DisplayName("수업 정보 변경")
    void changeClassInfo(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Register register = Register.of("00000000");
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, register);
        classroomModel = changeClassroomInfoService.changeClassInfo(ChangeClassInfo.builder()
                        .color(Color.SECONDARY)
                        .title("수정 수업명")
                .build(),ClassroomCode.of(classroomModel.getCode()), register);
        assertEquals(classroomModel.getClassInfo().getColor(), Color.SECONDARY);
        assertEquals(classroomModel.getClassInfo().getTitle(), "수정 수업명");
    }
}
