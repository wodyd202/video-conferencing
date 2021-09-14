package com.ljy.videoclass.user.domain.read;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ljy.videoclass.user.domain.value.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class UserModel {
    private String userId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private String username;

    @Builder
    public UserModel(UserId userId, Password password, Username username){
        this.userId = userId.get();
        if(!Objects.isNull(password)){
            this.password = password.get();
        }
        this.username = username.get();
    }
}
