package com.ljy.user.command.application.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterUser {
    private String userId;
    private String password;

    @Builder
    public RegisterUser(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
