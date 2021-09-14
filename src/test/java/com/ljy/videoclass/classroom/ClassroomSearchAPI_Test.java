package com.ljy.videoclass.classroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.classroom.command.application.OpenClassroomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void findByRegister() throws Exception {
        mockMvc.perform(get("/api/classroom"))
                .andExpect(status().isOk());
    }

}
