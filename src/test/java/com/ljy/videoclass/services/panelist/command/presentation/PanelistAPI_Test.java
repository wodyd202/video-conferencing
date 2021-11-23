package com.ljy.videoclass.services.panelist.command.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.services.classroom.command.application.model.SignUpPanalist;
import com.ljy.videoclass.services.panelist.PanelistAPITest;
import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.PanelistFixture;
import com.ljy.videoclass.services.panelist.domain.value.Email;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.ljy.videoclass.services.panelist.domain.PanelistFixture.aPanelist;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 회의자 API 테스트
 */
@AutoConfigureMockMvc
public class PanelistAPI_Test extends PanelistAPITest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 회의자_등록시_비밀번호를_입력하지_않으면_400_에러_반환() throws Exception {
        // given
        SignUpPanalist signUpPanalist = SignUpPanalist.builder()
                .email("email@google.com")
                .password("")
                .build();

        // when
        mockMvc.perform(post("/api/panelist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpPanalist)))

        // then
        .andExpect(status().isBadRequest());
    }

    @Test
    void 회의자_등록시_이메일을_입력하지_않으면_400_에러_반환() throws Exception {
        // given
        SignUpPanalist signUpPanalist = SignUpPanalist.builder()
                .email("")
                .password("password")
                .build();

        // when
        mockMvc.perform(post("/api/panelist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpPanalist)))

        // then
        .andExpect(status().isBadRequest());
    }

    @Test
    void 존재하지_않는_이메일로_회의자_등록() throws Exception{
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
