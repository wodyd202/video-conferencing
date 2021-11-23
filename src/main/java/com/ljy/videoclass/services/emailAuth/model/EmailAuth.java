package com.ljy.videoclass.services.emailAuth.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailAuth {
    private String targetEmail;
    private String authKey;

    @Builder
    public EmailAuth(String targetEmail, String authKey) {
        this.targetEmail = targetEmail;
        this.authKey = authKey;
    }
}
