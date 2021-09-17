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
    @DisplayName("수업 설명 포함")
    void changeClassInfo_contains_description(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Register register = Register.of("00000000");
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, register);
        classroomModel = changeClassroomInfoService.changeClassInfo(ChangeClassInfo.builder()
                .color(Color.secondary)
                .title("수정 수업명")
                .description("수업 설명")
                .build(),ClassroomCode.of(classroomModel.getCode()), register);
        assertEquals(classroomModel.getClassInfo().getColor(), Color.secondary);
        assertEquals(classroomModel.getClassInfo().getTitle(), "수정 수업명");
        assertEquals(classroomModel.getClassInfo().getDescription(), "수업 설명");
    }

    @Test
    @DisplayName("수업 설명 제외")
    void changeClassInfo_empty_description(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Register register = Register.of("00000000");
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, register);
        classroomModel = changeClassroomInfoService.changeClassInfo(ChangeClassInfo.builder()
                        .color(Color.secondary)
                        .title("수정 수업명")
                .build(),ClassroomCode.of(classroomModel.getCode()), register);
        assertEquals(classroomModel.getClassInfo().getColor(), Color.secondary);
        assertEquals(classroomModel.getClassInfo().getTitle(), "수정 수업명");
    }
}
