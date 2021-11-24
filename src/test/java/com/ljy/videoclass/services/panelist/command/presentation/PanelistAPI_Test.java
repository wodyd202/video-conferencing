package com.ljy.videoclass.services.panelist.command.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.services.panelist.command.model.SignUpPanalist;
import com.ljy.videoclass.services.panelist.PanelistAPITest;
import com.ljy.videoclass.services.panelist.domain.value.PanelistId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
                .id("panelistId")
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
    void 회의자_등록시_아이디를_입력하지_않으면_400_에러_반환() throws Exception {
        // given
        SignUpPanalist signUpPanalist = SignUpPanalist.builder()
                .id("")
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
                .id("signUpApi")
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
        newPanelist(aPanelist().id(PanelistId.of("alreadyExist")));

        // given
        SignUpPanalist signUpPanalist = SignUpPanalist.builder()
                .id("alreadyExist")
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
