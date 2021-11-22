package com.ljy.videoclass.services.panelist.command.application;

import com.ljy.videoclass.services.classroom.command.application.model.SignUpPanalist;
import com.ljy.videoclass.services.panelist.PanelistAPITest;
import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.PanelistFixture;
import com.ljy.videoclass.services.panelist.domain.exception.AlreadyExistPanelistException;
import com.ljy.videoclass.services.panelist.domain.value.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.ljy.videoclass.services.panelist.domain.PanelistFixture.aPanelist;
import static org.junit.Assert.assertTrue;

@SpringBootTest
public class SignUpPanelistService_Test extends PanelistAPITest {
    @Autowired
    private SignUpPanelistService signUpPanelistService;

    // 회의자 등록시 이미 해당 이메일로 등록한 회의자가 있는지 확인하고
    // 만약 있으면 에러 발생
    @Test
    void 회의자_등록시_중복된_이메일_존재하면_에러발생(){
        // given
        newPanelist(aPanelist().email(Email.of("test@google.com")));

        // when
        Assertions.assertThrows(AlreadyExistPanelistException.class, ()->{
            // 사용자 요청
            SignUpPanalist signUpPanalist = SignUpPanalist.builder()
                    .email("test@google.com")
                    .password("password")
                    .build();
            signUpPanelistService.signUp(signUpPanalist);
        });
    }

    @Test
    void 회의자_등록(){
        // given
        // 사용자 요청
        SignUpPanalist signUpPanalist = SignUpPanalist.builder()
                .email("notExist@google.com")
                .password("password")
                .build();

        // when
        signUpPanelistService.signUp(signUpPanalist);
        Optional<Panelist> panelistOptional = panelistRepository.findById(Email.of("notExist@google.com"));

        // then
        assertTrue(panelistOptional.isPresent());
    }
}
