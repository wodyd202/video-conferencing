package com.ljy.videoclass.services.user.domain;

import lombok.Builder;

public class OauthLoginUser {
    private String identifier;
    private String username;
    private String email;
    private String image;

    @Builder
    private OauthLoginUser(String identifier, String username, String email, String image) {
        this.identifier = identifier;
        this.username = username;
        this.email = email;
        this.image = image;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}
