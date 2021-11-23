package com.ljy.videoclass.services.emailAuth.model;

import org.junit.jupiter.api.Test;

import static com.ljy.videoclass.services.emailAuth.model.EmailAuthFixture.aEmailAuth;
import static org.junit.Assert.assertEquals;

/**
 * 이메일 인증 테스트
 */
public class EmailAuth_Test {

    @Test
    void 인증_정보_생성(){
        // when
        EmailAuth emailAuth = EmailAuth.builder()
                .targetEmail("email@google.com")
                .authKey("authKey")
                .build();

        // then
        assertEquals(emailAuth.getTargetEmail(), "email@google.com");
        assertEquals(emailAuth.getAuthKey(), "authKey");
    }

    @Test
    void 인증_완료(){
        // given
        EmailAuth emailAuth = aEmailAuth().authKey("authKey").build();

        // when
        emailAuth.attemptAuth("authKey");
    }
}
