package com.ljy.videoclass.user.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ljy.videoclass.user.domain.Password;
import com.ljy.videoclass.user.domain.UserId;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class UserModel {
    private String userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @Builder
    public UserModel(UserId userId, Password password){
        this.userId = userId.get();
        if(!Objects.isNull(password)){
            this.password = password.get();
        }
    }
}
