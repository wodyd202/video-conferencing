package com.ljy.videoclass.services.conferenceHistory.command.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.services.conferenceHistory.command.model.OpenConference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "개최자")
public class ConferenceAPI_Test {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void 회의_개최시_제목을_입력하지_않으면_400_에러_발생() throws Exception {
        // given
        OpenConference openConference = OpenConference.builder()
                .limitCount(2)
                .title("")
                .build();

        // when
        mockMvc.perform(post("/api/conference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openConference)))

                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    void 회의_개최시_제한인원이_2명이하일_경우_400_에러_발생() throws Exception {
        // given
        OpenConference openConference = OpenConference.builder()
                .limitCount(1)
                .title("회의 제목")
                .build();

        // when
        mockMvc.perform(post("/api/conference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openConference)))

        // then
        .andExpect(status().isBadRequest());
    }

    @Test
    void 회의_개최시_제한인원이_10명_초과일_경우_400_에러_발생() throws Exception {
        // given
        OpenConference openConference = OpenConference.builder()
                .limitCount(11)
                .title("회의 제목")
                .build();

        // when
        mockMvc.perform(post("/api/conference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openConference)))

        // then
        .andExpect(status().isBadRequest());
    }

    @Test
    void 회의_개최() throws Exception{
        // given
        OpenConference openConference = OpenConference.builder()
                .limitCount(2)
                .title("회의 제목")
                .build();

        // when
        mockMvc.perform(post("/api/conference")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openConference)))

        // then
        .andExpect(status().isOk());
    }
}
