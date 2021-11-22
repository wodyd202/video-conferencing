package com.ljy.videoclass.services.panelist.command.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.services.classroom.command.application.model.SignUpPanalist;
import com.ljy.videoclass.services.panelist.PanelistAPITest;
import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.PanelistFixture;
import com.ljy.videoclass.services.panelist.domain.value.Email;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.ljy.videoclass.services.panelist.domain.PanelistFixture.aPanelist;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
public class PanelistAPI_Test extends PanelistAPITest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 회의자_등록() throws Exception{
        // given
        SignUpPanalist signUpPanalist = SignUpPanalist.builder()
                .email("signUpApi@google.com")
                .password("password")
                .build();

        // when
        mockMvc.perform(post("/api/panelist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpPanalist)))

        // then
        .andExpect(status().isOk());
    }

    @Test
    void 회의자_등록시_중복된_이메일이_존재할_경우_400_에러_반환() throws Exception{
        newPanelist(aPanelist().email(Email.of("alreadyExist@google.com")));

        // given
        SignUpPanalist signUpPanalist = SignUpPanalist.builder()
                .email("alreadyExist@google.com")
                .password("password")
                .build();

        // when
        mockMvc.perform(post("/api/panelist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpPanalist)))

        // then
        .andExpect(status().isBadRequest());
    }
}
