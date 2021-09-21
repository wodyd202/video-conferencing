package com.ljy.videoclass.classroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.classroom.command.application.DisableClassroomService;
import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.ChangeClassInfo;
import com.ljy.videoclass.classroom.domain.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
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

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;
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

        assertRegister(openClassroom, status().isBadRequest());
    }

    @Test
    @DisplayName("수업 요일을 적어도 하나 이상 입력해야함")
    void register_emptyDayOfWeeks() throws Exception {
        OpenClassroom openClassroom = OpenClassroom.builder()
                .classInfo(ChangeClassInfo.builder()
                        .title("수업명")
                        .build())
                .classDateInfo(ChangeClassDateInfo.builder()
                        .startHour(14)
                        .endHour(15)
                        .build())
                .build();

        assertRegister(openClassroom, status().isBadRequest());
    }

    @Test
    @DisplayName("수업명은 반드시 입력해야함")
    void register_emptyTitle() throws Exception {
        OpenClassroom openClassroom = OpenClassroom.builder()
                .classInfo(ChangeClassInfo.builder()
                        .build())
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(14)
                        .endHour(15)
                        .build())
                .build();

        assertRegister(openClassroom, status().isBadRequest());
    }

    @Test
    void register() throws Exception{
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

        assertRegister(openClassroom, status().isOk());
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
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        mockMvc.perform(delete("/api/classroom/{classroomCode}", classroomModel.getCode()))
                .andExpect(status().isOk());
    }

    @Autowired
    DisableClassroomService disableClassroomService;

    @Test
    @DisplayName("수업 활성화")
    void active() throws Exception {
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));
        disableClassroomService.disable(ClassroomCode.of(classroomModel.getCode()), Register.of("00000000"));

        mockMvc.perform(put("/api/classroom/{classroomCode}", classroomModel.getCode()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("수업 정보 변경")
    void changeClassInfo() throws Exception {
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        mockMvc.perform(put("/api/classroom/{classroomCode}/class-info", classroomModel.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ChangeClassInfo.builder()
                        .title("수정 수업명")
                        .build())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("수업 시간 변경")
    void changeClassDateInfo() throws Exception {
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        mockMvc.perform(put("/api/classroom/{classroomCode}/class-date-info", classroomModel.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ChangeClassDateInfo.builder()
                                        .dayOfWeek(DayOfWeek.FRIDAY)
                                        .startHour(10)
                                        .endHour(15)
                                .build())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("수업 날짜 변경")
    void changeClassOptionalDateInfo() throws Exception {
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        mockMvc.perform(put("/api/classroom/{classroomCode}/class-optional-date-info",classroomModel.getCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ChangeClassOptionalDateInfo.builder()
                                        .startDate(LocalDate.of(2020,1,1))
                                        .endDate(LocalDate.of(2020,12,25))
                                        .autoEnabled(false)
                                .build())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("수강 신청")
    void elrolment() throws Exception {
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("elrolment"));

        mockMvc.perform(post("/api/classroom/{classroomCode}/elrolment",classroomModel.getCode()))
                .andExpect(status().isOk());
    }

}
