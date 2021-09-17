package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;
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
    void findByCode() throws Exception{
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

        mockMvc.perform(get("/api/classroom/{classroomCode}", classroomModel.getCode()))
                .andExpect(status().isOk());
    }

    @Test
    void findByClassDateAndDayOfWeek() throws Exception{
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

        mockMvc.perform(get("/api/classroom/exist-by")
                .param("startDate","2020-05-01")
                .param("endDate","2020-12-25")
                .param("dayOfWeek", "MONDAY")
                .param("startHour","1")
                .param("endHour", "4")
                .param("size", "10")
                .param("page", "0"))
            .andExpect(status().isOk());
    }

    @Test
    void findByRegister() throws Exception {
        mockMvc.perform(get("/api/classroom")
                        .param("state","Activate")
                        .param("page","0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
