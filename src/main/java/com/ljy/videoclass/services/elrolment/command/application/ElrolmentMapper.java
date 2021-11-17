package com.ljy.videoclass.services.elrolment.command.application;

import com.ljy.videoclass.services.elrolment.command.application.external.UserInfo;
import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.RequesterInfo;
import org.springframework.stereotype.Component;

@Component
public class ElrolmentMapper {

    public Elrolment mapFrom(ClassroomCode classCode, UserInfo userInfo) {
        return Elrolment.builder()
                .code(classCode)
                .requesterInfo(RequesterInfo.builder()
                        .requester(userInfo.getRequester())
                        .requesterEmail(userInfo.getRequesterEmail())
                        .requesterImage(userInfo.getRequesterImage())
                        .requesterName(userInfo.getRequesterName())
                        .build())
                .build();
    }
}
