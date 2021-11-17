package com.ljy.videoclass.services.elrolment.domain.read;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ElrolmentRequesterInfoModel {
    private String requester;
    private String requesterImage;
    private String requesterEmail;
    private String requesterName;

    @Builder
    public ElrolmentRequesterInfoModel(String requester, String requesterImage, String requesterEmail,String requesterName) {
        this.requester = requester;
        this.requesterImage = requesterImage;
        this.requesterEmail = requesterEmail;
        this.requesterName = requesterName;
    }

    @Override
    public String toString() {
        return "ElrolmentRequesterInfoModel{" +
                "requester='" + requester + '\'' +
                ", requesterImage='" + requesterImage + '\'' +
                ", requesterEmail='" + requesterEmail + '\'' +
                ", requesterName='" + requesterName + '\'' +
                '}';
    }
}
