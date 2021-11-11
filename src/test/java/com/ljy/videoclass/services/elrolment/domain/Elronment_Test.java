package com.ljy.videoclass.services.elrolment.domain;

import com.ljy.videoclass.services.classroom.ClassroomFixture;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import com.ljy.videoclass.services.elrolment.ElrolmentFixture;
import com.ljy.videoclass.services.elrolment.application.DefaultRequestElrolmentValidator;
import com.ljy.videoclass.services.elrolment.application.ElrolmentMapper;
import com.ljy.videoclass.services.elrolment.application.StubClassroomRepository;
import com.ljy.videoclass.services.elrolment.application.StubElrolmentRepository;
import com.ljy.videoclass.services.elrolment.command.application.ElrolmentRepository;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentRequesterInfoModel;
import com.ljy.videoclass.services.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.services.elrolment.domain.value.RequesterInfo;
import com.ljy.videoclass.services.elrolment.query.application.ClassroomRepository;
import org.junit.jupiter.api.Test;

import static com.ljy.videoclass.services.elrolment.ElrolmentFixture.aElrolment;
import static com.ljy.videoclass.services.elrolment.ElrolmentFixture.aRequester;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Elronment_Test {
    ElrolmentMapper elrolmentFactory = new ElrolmentMapper();

    @Test
    void 수강신청_생성(){
        // given
        ClassroomModel classroomModel = ClassroomFixture.aClassroom(Register.of("생성자")).toModel();
        String classCode = classroomModel.getCode();
        RequesterInfo requesterInfo = RequesterInfo.builder()
                .requester("requester")
                .requesterEmail("requesterEmail")
                .requesterImage("requesterImage")
                .requesterName("requesterName")
                .build();

        // when
        Elrolment elrolment = elrolmentFactory.mapFrom(classCode, requesterInfo);
        ElrolmentModel elrolmentModel = elrolment.toModel();

        // then
        ElrolmentRequesterInfoModel requester = elrolmentModel.getRequester();
        assertEquals(requester.getRequester(), "requester");
        assertEquals(requester.getRequesterEmail(), "requesterEmail");
        assertEquals(requester.getRequesterImage(), "requesterImage");
        assertEquals(requester.getRequesterName(), "requesterName");
        assertNull(elrolmentModel.getState());
        assertEquals(elrolmentModel.getClassroomCode(), classCode);
    }

    @Test
    void 수강_신청(){
        // given
        ClassroomRepository classroomRepository = new StubClassroomRepository();
        ElrolmentRepository elrolmentRepository = new StubElrolmentRepository();
        RequestElrolmentValidator requestElrolmentValidator = new DefaultRequestElrolmentValidator(classroomRepository, elrolmentRepository);

        Elrolment elrolment = aElrolment("classroomCode", aRequester().build());

        // when
        elrolment.request(requestElrolmentValidator);
        ElrolmentModel elrolmentModel = elrolment.toModel();

        // then
        assertEquals(elrolmentModel.getState(), ElrolmentState.NOT);
    }
}
