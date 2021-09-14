package com.ljy.videoclass.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterUser {
    private String userId;
    private String password;
    private String name;

    @Builder
    public RegisterUser(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }
}
