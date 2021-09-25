package com.ljy.videoclass.elrolment.domain.value;

import com.ljy.videoclass.elrolment.domain.read.ElrolmentRequesterInfoModel;
import lombok.Builder;

import javax.persistence.Embeddable;

@Embeddable
public class RequesterInfo {
    private final String requester;
    private String requesterImage;
    private final String requesterEmail;
    private final String requesterName;

    protected RequesterInfo(){
        this.requesterName = null;
        requesterEmail = null; requester = null;
    }

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
