package com.ljy.videoclass.services.classroom;

import com.ljy.videoclass.services.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.ljy.videoclass.services.classroom.ClassroomFixture.aOpenClassroom;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "00000000")
public class ClassroomSearchAPI_Test {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    OpenClassroomService openClassroomService;

    @Test
    @DisplayName("해당 코드의 수업 정보 조회")
    void findByCode() throws Exception{
        // given
        OpenClassroom openClassroom = aOpenClassroom()
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(1)
                        .endHour(4)
                        .build())
                .classOptionalDateInfo(ChangeClassOptionalDateInfo.builder()
                        .autoEnabled(false)
                        .startDate(LocalDate.of(2020,5,1))
                        .endDate(LocalDate.of(2020,12,25))
                        .build()).build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        // when
        mockMvc.perform(get("/api/classroom/{classroomCode}", classroomModel.getCode()))

        // then
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("입력한 일자 사이에 존재하는 수업 정보 리스트 조회")
    void findByClassDateAndDayOfWeek() throws Exception{
        // given
        OpenClassroom openClassroom = aOpenClassroom()
                .classDateInfo(ChangeClassDateInfo.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startHour(1)
                        .endHour(4)
                        .build())
                .classOptionalDateInfo(ChangeClassOptionalDateInfo.builder()
                        .autoEnabled(false)
                        .startDate(LocalDate.of(2020,5,1))
                        .endDate(LocalDate.of(2020,12,25))
                .build()).build();
        openClassroomService.open(openClassroom, Register.of("00000000"));

        // when
        mockMvc.perform(get("/api/classroom/exist-by")
                .param("startDate","2020-05-01")
                .param("endDate","2020-12-25")
                .param("dayOfWeek", "MONDAY")
                .param("startHour","1")
                .param("endHour", "4")
                .param("size", "10")
                .param("page", "0"))

        // then
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("자신의 활성화된 수업 정보 리스트 조회")
    void findByRegister() throws Exception {
        // when
        mockMvc.perform(get("/api/classroom")
                        .param("state","Activate")
                        .param("page","0")
                        .param("size", "10"))

        // then
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("자신의 지난 수업 정보 리스트 조회")
    void findLastClassroomByRegister() throws Exception {
        // when
        mockMvc.perform(get("/api/classroom/last")
                        .param("page","0")
                        .param("size","10"))

        // then
        .andExpect(status().isOk());
    }

}
