package com.ljy.videoclass.services.emailAuth.model;

public class EmailAuthFixture {
    public static EmailAuth.EmailAuthBuilder aEmailAuth() {
        return EmailAuth.builder()
                .authKey("authKey")
                .targetEmail("targetEmail");
    }
}
