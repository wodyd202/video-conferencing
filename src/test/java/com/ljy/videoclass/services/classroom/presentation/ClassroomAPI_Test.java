package com.ljy.videoclass.services.classroom.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.services.classroom.command.application.ChangeClassroomService;
import com.ljy.videoclass.services.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.ljy.videoclass.services.classroom.ClassroomFixture.aOpenClassroom;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "00000000")
public class ClassroomAPI_Test {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("수업 시작시간은 종료시간보다 작아야함")
    void register_invalidClassTime() throws Exception {
        // given
        OpenClassroom openClassroom = OpenClassroom.builder()
                .classInfo(ChangeClassInfo.builder()
                        .title("수업명")
                        .build())
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(17)
                        .endHour(15)
                        .build())
                .build();

        // when
        assertRegister(openClassroom,

        // then
        status().isBadRequest());
    }

    @Test
    @DisplayName("수업 요일을 적어도 하나 이상 입력해야함")
    void register_emptyDayOfWeeks() throws Exception {
        // given
        OpenClassroom openClassroom = OpenClassroom.builder()
                .classInfo(ChangeClassInfo.builder()
                        .title("수업명")
                        .build())
                .classDateInfo(ChangeClassDateInfo.builder()
                        .startHour(14)
                        .endHour(15)
                        .build())
                .build();

        // when
        assertRegister(openClassroom,

        // then
        status().isBadRequest());
    }

    @Test
    @DisplayName("수업명은 반드시 입력해야함")
    void register_emptyTitle() throws Exception {
        // given
        OpenClassroom openClassroom = OpenClassroom.builder()
                .classInfo(ChangeClassInfo.builder()
                        .build())
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(14)
                        .endHour(15)
                        .build())
                .build();

        // when
        assertRegister(openClassroom,

        // then
        status().isBadRequest());
    }

    @Test
    void register() throws Exception{
        // given
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

        // when
        assertRegister(openClassroom,

        // then
        status().isOk());
    }

    private void assertRegister(OpenClassroom openClassroom, ResultMatcher resultMatcher) throws Exception{
        mockMvc.perform(post("/api/classroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openClassroom)))
                .andExpect(resultMatcher);
    }

    @Autowired
    OpenClassroomService openClassroomService;

    @Test
    @DisplayName("수업 비활성화")
    void disable() throws Exception {
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        // when
        mockMvc.perform(delete("/api/classroom/{classroomCode}", classroomModel.getCode()))

        // then
        .andExpect(status().isOk());
    }

    @Autowired
    ChangeClassroomService changeClassroomService;

    @Test
    @DisplayName("수업 활성화")
    void active() throws Exception {
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));
        changeClassroomService.disable(ClassroomCode.of(classroomModel.getCode()), Register.of("00000000"));

        //when
        mockMvc.perform(put("/api/classroom/{classroomCode}", classroomModel.getCode()))

        // then
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("수업 정보 변경")
    void changeClassInfo() throws Exception {
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        // when
        mockMvc.perform(put("/api/classroom/{classroomCode}/class-info", classroomModel.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ChangeClassInfo.builder()
                        .title("수정 수업명")
                        .build())))
        // then
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("수업 시간 변경")
    void changeClassDateInfo() throws Exception {
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        // when
        mockMvc.perform(put("/api/classroom/{classroomCode}/class-date-info", classroomModel.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ChangeClassDateInfo.builder()
                                        .dayOfWeek(DayOfWeek.FRIDAY)
                                        .startHour(10)
                                        .endHour(15)
                                .build())))

        // then
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("수업 날짜 변경")
    void changeClassOptionalDateInfo() throws Exception {
        // given
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        // when
        mockMvc.perform(put("/api/classroom/{classroomCode}/class-optional-date-info",classroomModel.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ChangeClassOptionalDateInfo.builder()
                                        .startDate(LocalDate.of(2020,1,1))
                                        .endDate(LocalDate.of(2020,12,25))
                                        .autoEnabled(false)
                                .build())))

        // then
        .andExpect(status().isOk());
    }
}
