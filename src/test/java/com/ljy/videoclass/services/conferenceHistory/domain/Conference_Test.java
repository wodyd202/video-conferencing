package com.ljy.videoclass.services.conferenceHistory.domain;

import com.ljy.videoclass.services.conferenceHistory.command.application.ConferenceMapper;
import com.ljy.videoclass.services.conferenceHistory.command.model.OpenConference;
import com.ljy.videoclass.services.conferenceHistory.domain.model.ConferenceModel;
import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceTitle;
import com.ljy.videoclass.services.conferenceHistory.domain.value.Creator;
import com.ljy.videoclass.services.conferenceHistory.domain.value.LimitCount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * 회의 도메인 테스트
 */
public class Conference_Test {

    // 회의실 제목은 한글, 숫자, 영어(공백문자 허용) 조합으로 입력해야한다.
    // 그렇지 않으면 IllegalArgumentException을 발생시킨다.
    @ParameterizedTest
    @ValueSource(strings = {
            "invalid@",
            "!@회의실 제목"
    })
    void 회의실_제목은_한글_숫자_영어_조합만_허용(String title){
        // when
        assertThrows(IllegalArgumentException.class, ()->{
            ConferenceTitle.of(title);
        });
    }

    // 회의실 제목은 5자 이상 20자 이하로 입력해야한다.
    // 그렇지 않으면 IllegalArgumentException을 발생시킨다.
    @Test
    void 회의실은_5자이상_20자_이하만_허용(){
        // when
        assertThrows(IllegalArgumentException.class, ()->{
            ConferenceTitle.of("회의실은 5자이상 20자 이하만 허용회의실은 5자이상 20자 이하만 허용");
        });
    }

    // 회의실 제한인원은 반드시 2명 이상으로 입력해야한다.
    // 그렇지 않으면 IllegalArgumentException을 발생시킨다.
    @Test
    void 제한_인원은_2명이상으로_입력해야함(){
        // when
        assertThrows(IllegalArgumentException.class, ()->{
            LimitCount.of(1);
        });
    }

    // 회의실 제한인원은 반드시 10명 이하로 입력해야한다.
    // 그렇지 않으면 IllegalArgumentException을 발생시킨다.
    @Test
    void 제한_인원은_최대_10명_까지_허용(){
        assertThrows(IllegalArgumentException.class, ()->{
            LimitCount.of(11);
        });
    }

    @Test
    void 회의실_생성(){
        // when
        Conference conference = Conference.builder()
                .code(ConferenceCode.of("code"))
                .title(ConferenceTitle.of("회의실 제목"))
                .limitCount(LimitCount.of(2))
                .build();
        conference.open(Creator.of("생성자"), mock(OpenConferenceValidator.class));
        ConferenceModel conferenceModel = conference.toModel();

        // then
        assertEquals(conferenceModel.getCode(), "code");
        assertEquals(conferenceModel.getTitle(), "회의실 제목");
        assertEquals(conferenceModel.getLimitCount(), 2);
    }

    @Test
    void 회의실_매퍼_테스트(){
        // given
        OpenConference openConference = OpenConference.builder()
                .title("회의실 제목")
                .limitCount(2)
                .build();
        ConferenceMapper conferenceMapper = new ConferenceMapper();

        // when
        Conference conference = conferenceMapper.mapFrom(ConferenceCode.of("code"), openConference);
        conference.open(Creator.of("생성자"), mock(OpenConferenceValidator.class));
        ConferenceModel conferenceModel = conference.toModel();

        // then
        assertEquals(conferenceModel.getCode(), "code");
        assertEquals(conferenceModel.getTitle(), "회의실 제목");
        assertEquals(conferenceModel.getLimitCount(), 2);
    }
}

