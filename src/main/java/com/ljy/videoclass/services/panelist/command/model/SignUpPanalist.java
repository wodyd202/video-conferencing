package com.ljy.videoclass.services.panelist.command.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class SignUpPanalist {
    @NotBlank(message = "회의자의 아이디를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,15}$", message = "회의자의 아이디는 [영어, 숫자] 조합으로 5자 이상 15자 이하로 입력해주세요.")
    private String id;

    @NotBlank(message = "회의자의 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9~!@#$%^&]{8,15}$", message = "회의자의 비밀번호는 [영어, 숫자, 특수문자(~ ! @ # $ % ^ &)] 조합으로 8자 이상 15자 이하로 입력해주세요.")
    private String password;

    @Builder
    public SignUpPanalist(String id, String password) {
        this.id = id;
        this.password = password;
    }

    @Override
    public String toString() {
        return "SignUpPanalist{" +
                "email='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
