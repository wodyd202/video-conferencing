package com.ljy.videoclass.services.panelist.domain;

import com.ljy.videoclass.services.panelist.command.application.PanelistMapper;
import com.ljy.videoclass.services.panelist.command.model.SignUpPanalist;
import com.ljy.videoclass.services.panelist.domain.model.PanelistModel;
import com.ljy.videoclass.services.panelist.domain.value.PanelistId;
import com.ljy.videoclass.services.panelist.domain.value.Password;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.ljy.videoclass.services.panelist.domain.value.PanelistStatus.ACTIVE;
import static com.ljy.videoclass.services.panelist.domain.value.PanelistStatus.DE_ACTIVE;
import static com.ljy.videoclass.services.panelist.domain.PanelistFixture.aPanelist;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * 회의자 도메인 테스트
 */
public class Panalist_Test {

    @ParameterizedTest
    @ValueSource(strings = {
            "@panelistId",
            "유효하지 않음"
    })
    void 회의자_아이디는_영어및숫자조합만_허용함(String panelistId){
        // when
        assertThrows(IllegalArgumentException.class, ()->{
            PanelistId.of(panelistId);
        });
    }

    // 회의자 도메인을 읽기 전용 모델로 변환한다.
    // 입력받았던 데이터와 읽기 전용 모델의 데이터가 일치해야한다.
    @Test
    void 회의자_생성(){
        // given
        Panelist panelist = Panelist.builder()
                .id(PanelistId.of("testId"))
                .password(Password.of("password", mock(PasswordEncoder.class)))
                .build();

        // when
        PanelistModel panalistModel = panelist.toModel();

        // then
        assertEquals(panalistModel.getId(), "testId");
    }

    // 회의자 생성 요청을 받는다.
    // 생성 요청을 Panelist로 변환후 읽기 전용 Model을 반환한다.
    // 반환받은 Model과 생성 요청한 데이터가 일치해야한다.
    @Test
    void 회의자_매퍼_테스트(){
        // given
        // 생성 요청
        SignUpPanalist signUpPanalist = SignUpPanalist.builder()
                .id("testId")
                .password("password")
                .build();

        PanelistMapper panalistMapper = new PanelistMapper();

        // when
        Panelist panelist = panalistMapper.mapFrom(signUpPanalist, mock(PasswordEncoder.class));
        PanelistModel panalistModel = panelist.toModel();

        // then
        assertEquals(panalistModel.getId(), "testId");
    }

    // 추방 당할 경우 추방 카운트를 1씩 증가시킨다.
    @Test
    void 추방(){
        // given
        Panelist panelist = aPanelist().build();

        // when
        panelist.expell();
        PanelistModel panalistModel = panelist.toModel();

        // then
        assertEquals(panalistModel.getExpellCount(), 1);
    }

    // 추방 횟수가 5회 이상일 경우
    // 해당 회의자의 추방 카운트는 0이 되고 해당 계정은 비활성화 처리된다.
    @Test
    void 경고로인한_회의자_비활성화(){
        // given
        Panelist panelist = aPanelist().build();

        // when
        panelist.expell();
        panelist.expell();
        panelist.expell();
        panelist.expell();
        panelist.expell();
        PanelistModel panalistModel = panelist.toModel();

        // then
        assertEquals(panalistModel.getExpellCount(), 0);
        assertEquals(panalistModel.getStatus(), DE_ACTIVE);
    }

    // 추방으로 인해 비활성화된 계정은 비활성화가 된 날짜로 부터 일주일이 지나면
    // 다시 활성화 상태가 된다.
    @Test
    void 계정_활성화(){
        // given
        Panelist panelist = warnningPanelist(aPanelist());

        // when
        panelist.activation();
        PanelistModel panelistModel = panelist.toModel();

        // then
        assertEquals(panelistModel.getStatus(), ACTIVE);
    }

    private Panelist warnningPanelist(Panelist.PanelistBuilder panelistBuilder){
        Panelist panelist = panelistBuilder.build();
        panelist.expell();
        panelist.expell();
        panelist.expell();
        panelist.expell();
        panelist.expell();
        return panelist;
    }
}
