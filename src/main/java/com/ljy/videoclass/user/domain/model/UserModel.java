package com.ljy.videoclass.user.query.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserModel {
    private String userId;
    private String password;
}
