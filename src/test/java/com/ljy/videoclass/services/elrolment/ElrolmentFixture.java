package com.ljy.videoclass.services.elrolment;

import com.ljy.videoclass.services.elrolment.command.application.ElrolmentMapper;
import com.ljy.videoclass.services.elrolment.command.application.external.Classroom;
import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.RequesterInfo;

import java.time.LocalDate;

public class ElrolmentFixture {
    private static ElrolmentMapper elrolmentMapper = new ElrolmentMapper();

    public static RequesterInfo.RequesterInfoBuilder aRequester() {
        return RequesterInfo.builder()
                .requesterName("requesterName")
                .requesterImage("requesterImage")
                .requesterEmail("requesterEmail")
                .requester("requester");
    }

    public static Elrolment.ElrolmentBuilder aElrolment() {
        return Elrolment.builder()
                .requesterInfo(aRequester().build())
                .code(ClassroomCode.of("classroomCode"));
    }

    public static Classroom.ClassroomBuilder aClassroom(){
        return Classroom.builder()
                .owner("")
                .classOptionalDateInfo(new Classroom.ClassOptionalDateInfo(LocalDate.now().plusDays(3)))
                .state(Classroom.ClassroomState.Activate)
                .classroomCode("classroomCode");
    }
}
