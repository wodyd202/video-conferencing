package com.ljy.videoclass.services.panelist;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpPanalist {
    private String email;
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
