package com.ljy.videoclass.enrollment;

import com.ljy.videoclass.classroom.ClassroomFixture;
import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.enrollment.domain.value.EnrollmentState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "00000000")
public class EnrollmentAPI_Test {
    @Autowired MockMvc mockMvc;
    @Autowired OpenClassroomService openClassroomService;

    @Test
    void enrollment() throws Exception{
        OpenClassroom openClassroom = ClassroomFixture.aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000001"));

        mockMvc.perform(post("/api/classroom/{classroomCode}/enrollment",classroomModel.getCode()))
                .andExpect(status().isOk());
    }

    @Test
    void findByCode() throws Exception {
        OpenClassroom openClassroom = ClassroomFixture.aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        mockMvc.perform(get("/api/classroom/{classroomCode}/enrollment/request", classroomModel.getCode())
                        .param("state", EnrollmentState.Disapproval.toString()))
                .andExpect(status().isOk());
    }
}
