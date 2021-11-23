package com.ljy.videoclass.services.classroom.command.application.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class SignUpPanalist {
    @NotBlank(message = "회의자의 이메일을 입력해주세요.")
    @Pattern(regexp = "[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "회의자의 이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "회의자의 비밀번호를 입력해주세요.")
    private String password;

    @Builder
    public SignUpPanalist(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "SignUpPanalist{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
