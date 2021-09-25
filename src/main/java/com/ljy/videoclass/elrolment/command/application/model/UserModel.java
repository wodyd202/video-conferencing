package com.ljy.videoclass.elrolment.command.application.model;

import com.ljy.videoclass.user.domain.value.Email;
import com.ljy.videoclass.user.domain.value.Image;
import com.ljy.videoclass.user.domain.value.Username;

public class UserModel {
    private String id;
    private String email;
    private String image;
    private String username;

    public UserModel(String id, Email email, Image image, Username username) {
        this.id = id;
        this.email = email.get();
        this.image = image.get();
        this.username = username.get();
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}
