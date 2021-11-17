package com.ljy.videoclass.services.elrolment.command.application.external;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfo {
    private String requester;
    private String requesterImage;
    private String requesterEmail;
    private String requesterName;

    @Builder
    public UserInfo(String requester, String requesterImage, String requesterEmail, String requesterName) {
        this.requester = requester;
        this.requesterImage = requesterImage;
        this.requesterEmail = requesterEmail;
        this.requesterName = requesterName;
    }
}
