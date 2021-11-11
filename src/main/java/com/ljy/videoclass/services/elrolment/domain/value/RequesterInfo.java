package com.ljy.videoclass.services.elrolment.domain.value;

import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentRequesterInfoModel;
import lombok.Builder;

import javax.persistence.Embeddable;

@Embeddable
public class RequesterInfo {
    private String requester;
    private String requesterImage;
    private String requesterEmail;
    private String requesterName;

    protected RequesterInfo(){}

    @Builder
    private RequesterInfo(String requester, String requesterImage, String requesterEmail, String requesterName) {
        this.requester = requester;
        this.requesterImage = requesterImage;
        this.requesterEmail = requesterEmail;
        this.requesterName = requesterName;
    }

    public ElrolmentRequesterInfoModel toModel() {
        return ElrolmentRequesterInfoModel.builder()
                .requester(requester)
                .requesterEmail(requesterEmail)
                .requesterImage(requesterImage)
                .requesterName(requesterName)
                .build();
    }
}
