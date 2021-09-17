package com.ljy.videoclass.user.domain.read;


import lombok.Builder;

public class UserModel {
    private String userId;
    private UserInfoModel userInfo;

    @Builder
    public UserModel(String userId, UserInfoModel userInfo) {
        this.userId = userId;
        this.userInfo = userInfo;
    }

    public String getUserId() {
        return userId;
    }

    public UserInfoModel getUserInfo() {
        return userInfo;
    }
}
