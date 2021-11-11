package com.ljy.videoclass.services.classroom.application;

import com.ljy.videoclass.services.classroom.ClassroomTest;
import com.ljy.videoclass.services.classroom.command.application.ChangeClassroomService;
import com.ljy.videoclass.services.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.services.classroom.domain.read.ClassDateInfoModel;
import com.ljy.videoclass.services.classroom.domain.read.ClassOptionalDateInfoModel;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomState;
import com.ljy.videoclass.services.classroom.domain.value.Color;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.ljy.videoclass.services.classroom.ClassroomFixture.aOpenClassroom;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class ChangeClassroomService_Test extends ClassroomTest {
    @Autowired OpenClassroomService openClassroomService;
    @Autowired ChangeClassroomService changeClassroomService;

    @Test
    @DisplayName("수업 비활성화")
    void disable(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));
        changeClassroomService.disable(ClassroomCode.of(classroomModel.getCode()), Register.of("00000000"));
    }

    @Test
    @DisplayName("강의 활성화")
    void active(){
        // given
        ClassroomModel classroomModel = newClassroom(aOpenClassroom().build(), Register.of("00000000"));
        changeClassroomService.disable(ClassroomCode.of(classroomModel.getCode()), Register.of("00000000"));

        // when
        changeClassroomService.active(ClassroomCode.of(classroomModel.getCode()), Register.of("00000000"));

        // then
        ClassroomModel classroom = findClassroom(classroomModel.getCode()).toModel();
        assertEquals(classroom.getState(), ClassroomState.Activate);
    }

    @Test
    @DisplayName("강의 날짜 변경시 자신의 강의만 변경할 수 있음")
    void changeClassDateInfo_throw(){
        assertThrows(ClassroomNotFoundException.class,()->{
            // given
            ClassroomModel classroomModel = newClassroom(aOpenClassroom().build(), Register.of("00000000"));

            // when
            changeClassroomService.changeClassDateInfo(ChangeClassDateInfo.builder()
                            .endHour(17)
                            .startHour(16)
                            .dayOfWeek(DayOfWeek.SUNDAY)
                            .build(),
                    ClassroomCode.of(classroomModel.getCode()),
                    Register.of("other"));
        });
    }

    @Test
    @DisplayName("강의 날짜 변경")
    void changeClassDateInfo(){
        // given
        ClassroomModel classroomModel = newClassroom(aOpenClassroom().build(), Register.of("00000000"));

        // when
        classroomModel = changeClassroomService.changeClassDateInfo(ChangeClassDateInfo.builder()
                        .endHour(17)
                        .startHour(16)
                        .dayOfWeek(DayOfWeek.SUNDAY)
                        .build(),
                ClassroomCode.of(classroomModel.getCode()),
                Register.of("00000000"));

        // then
        ClassDateInfoModel classDateInfo = classroomModel.getClassDateInfo();
        assertEquals(classDateInfo.getStartHour(), 16);
        assertEquals(classDateInfo.getEndHour(), 17);
        assertEquals(classDateInfo.getDayOfWeek(), DayOfWeek.SUNDAY);
    }

    @Test
    @DisplayName("상세 날짜 변경시 자신의 강의만 변경할 수 있음")
    void changeOptioanlDateInfo_throw(){
        assertThrows(ClassroomNotFoundException.class,()->{
            // given
            ClassroomModel classroomModel = newClassroom(aOpenClassroom().build(), Register.of("00000000"));

            // when
            changeClassroomService.changeClassOptionalDateInfo(ChangeClassOptionalDateInfo.builder()
                    .autoEnabled(false)
                    .startDate(LocalDate.of(2020,1,1))
                    .endDate(LocalDate.of(2020,12,25))
                    .build(), ClassroomCode.of(classroomModel.getCode()),Register.of("other"));
        });
    }

    @Test
    @DisplayName("강의 상세 날짜 변경")
    void changeOptioanlDateInfo_notEmpty(){
        // given
        ClassroomModel classroomModel = newClassroom(aOpenClassroom().build(), Register.of("00000000"));

        // when
        ChangeClassOptionalDateInfo classOptionalDateInfo = ChangeClassOptionalDateInfo.builder()
                .autoEnabled(false)
                .startDate(LocalDate.of(2020,1,1))
                .endDate(LocalDate.of(2020,12,25))
                .build();
        classroomModel = changeClassroomService.changeClassOptionalDateInfo(classOptionalDateInfo, ClassroomCode.of(classroomModel.getCode()),Register.of("00000000"));

        // then
        ClassOptionalDateInfoModel classOptionalDateInfoModel = classroomModel.getClassOptionalDateInfo();
        assertEquals(classOptionalDateInfoModel.getStartDate(), LocalDate.of(2020,1,1));
        assertEquals(classOptionalDateInfoModel.getEndDate(), LocalDate.of(2020,12,25));
        assertFalse(classOptionalDateInfoModel.isAutoEnabled());
    }

    @Test
    @DisplayName("강의 상세 날짜 제거")
    void changeOptioanlDateInfo_empty(){
        // given
        ClassroomModel classroomModel = newClassroom(aOpenClassroom().build(), Register.of("00000000"));

        // when
        classroomModel = changeClassroomService.changeClassOptionalDateInfo(null, ClassroomCode.of(classroomModel.getCode()),Register.of("00000000"));

        // then
        ClassOptionalDateInfoModel classOptionalDateInfoModel = classroomModel.getClassOptionalDateInfo();
        assertNull(classOptionalDateInfoModel.getStartDate());
        assertNull(classOptionalDateInfoModel.getEndDate());
        assertNull(classOptionalDateInfoModel.isAutoEnabled());
    }

    @Test
    @DisplayName("수업 설명 변경시 자신의 강의만 변경 가능")
    void changeClassInfo_contains_description_throw(){
        assertThrows(ClassroomNotFoundException.class, ()->{
            // given
            ClassroomModel classroomModel = newClassroom(aOpenClassroom().build(), Register.of("00000000"));

            // when
            changeClassroomService.changeClassInfo(ChangeClassInfo.builder()
                    .color(Color.secondary)
                    .title("수정 수업명")
                    .description("수업 설명")
                    .build(),ClassroomCode.of(classroomModel.getCode()), Register.of("other"));
        });
    }

    @Test
    @DisplayName("수업 설명 포함")
    void changeClassInfo_contains_description(){
        // given
        ClassroomModel classroomModel = newClassroom(aOpenClassroom().build(), Register.of("00000000"));

        // when
        classroomModel = changeClassroomService.changeClassInfo(ChangeClassInfo.builder()
                .color(Color.secondary)
                .title("수정 수업명")
                .description("수업 설명")
                .build(),ClassroomCode.of(classroomModel.getCode()), Register.of("00000000"));

        // then
        assertEquals(classroomModel.getClassInfo().getColor(), Color.secondary);
        assertEquals(classroomModel.getClassInfo().getTitle(), "수정 수업명");
        assertEquals(classroomModel.getClassInfo().getDescription(), "수업 설명");
    }

    @Test
    @DisplayName("수업 설명 제외")
    void changeClassInfo_empty_description(){
        // given
        ClassroomModel classroomModel = newClassroom(aOpenClassroom().build(), Register.of("00000000"));

        // when
        classroomModel = changeClassroomService.changeClassInfo(ChangeClassInfo.builder()
                .color(Color.secondary)
                .title("수정 수업명")
                .build(),ClassroomCode.of(classroomModel.getCode()), Register.of("00000000"));

        // then
        assertEquals(classroomModel.getClassInfo().getColor(), Color.secondary);
        assertEquals(classroomModel.getClassInfo().getTitle(), "수정 수업명");
    }
}
