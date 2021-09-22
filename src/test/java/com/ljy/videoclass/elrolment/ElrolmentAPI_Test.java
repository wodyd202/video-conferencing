package com.ljy.videoclass.elrolment;

import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.Register;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "00000000")
public class ElrolmentAPI_Test {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    OpenClassroomService openClassroomService;

    @Test
    void findByCode() throws Exception {
        OpenClassroom openClassroom = aOpenClassroom().build();
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of("00000000"));

        mockMvc.perform(get("/api/elrolment/{classroomCode}", classroomModel.getCode()))
                .andExpect(status().isOk());
    }
}
