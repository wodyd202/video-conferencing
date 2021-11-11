package com.ljy.videoclass.services.user.domain.read;

import lombok.Builder;

public class UserInfoModel {
    private String email;
    private String username;
    private String image;

    @Builder
    public UserInfoModel(String email, String username, String image) {
        this.email = email;
        this.username = username;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }
}
