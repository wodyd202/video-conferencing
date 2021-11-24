package com.ljy.videoclass.services.conference.command.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class OpenConference {
    @NotBlank(message = "회의 제목을 입력해주세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\s]{5,20}$",message = "회의 제목은 [한글,숫자,영어] 조합으로 5자 이상 20자 이하로 입력해주세요.")
    private String title;

    @Min(value = 2, message = "회의 제한 인원은 2인 이상 10인 이하로 입력해주세요.")
    @Max(value = 10, message = "회의 제한 인원은 2인 이상 10인 이하로 입력해주세요.")
    private int limitCount;

    @Builder
    public OpenConference(String title, int limitCount) {
        this.title = title;
        this.limitCount = limitCount;
    }

    @Override
    public String toString() {
        return "OpenConference{" +
                "title='" + title + '\'' +
                ", limitCount=" + limitCount +
                '}';
    }
}
