package com.ljy.videoclass.services.elrolment;

import com.ljy.videoclass.services.elrolment.application.ElrolmentMapper;
import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.value.RequesterInfo;

public class ElrolmentFixture {
    private static ElrolmentMapper elrolmentMapper = new ElrolmentMapper();
    public static RequesterInfo.RequesterInfoBuilder aRequester() {
        return RequesterInfo.builder()
                .requesterName("requesterName")
                .requesterImage("requesterImage")
                .requesterEmail("requesterEmail")
                .requester("requester");
    }

    public static Elrolment aElrolment(String classroomCode, RequesterInfo requesterInfo) {
        return elrolmentMapper.mapFrom(classroomCode, requesterInfo);
    }
}
