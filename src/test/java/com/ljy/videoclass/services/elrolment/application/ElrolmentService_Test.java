package com.ljy.videoclass.services.elrolment.application;

import com.ljy.videoclass.services.elrolment.ElrolmentTest;
import com.ljy.videoclass.services.elrolment.command.application.ElrolmentRepository;
import com.ljy.videoclass.services.elrolment.command.application.ElrolmentService;
import com.ljy.videoclass.services.elrolment.command.application.external.UserInfo;
import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.RequestElrolmentValidator;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.Requester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.ljy.videoclass.services.elrolment.ElrolmentFixture.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 수강신청 서비스 테스트
 */
@SpringBootTest
public class ElrolmentService_Test extends ElrolmentTest {
    @Autowired
    ElrolmentService elrolmentService;

    @Autowired
    ElrolmentRepository elrolmentRepository;

    @Autowired
    StubExternalUserRepository userRepository;

    @Autowired
    StubExternalClassroomRepository stubExternalClassroomRepository;

    @Test
    void 사용자의_정보가_존재하지_않음(){
        boolean existUser = userRepository.getUser("requester").isPresent();
        assertFalse(existUser);
    }

    @Test
    void 사용자_외부_모듈로_부터_정보를_가져옴(){
        // when
        newUser("requester");
        UserInfo requesterInfo = userRepository.getUser("requester").get();

        // then
        assertNotNull(requesterInfo.getRequester());
        assertNotNull(requesterInfo.getRequester());
        assertNotNull(requesterInfo.getRequester());
        assertNotNull(requesterInfo.getRequester());
    }

    @Test
    void 수강신청(){
        // given
        newClassroom(aClassroom().classroomCode("classroomCode"));

        // when
        ElrolmentModel elrolmentModel = elrolmentService.request(ClassroomCode.of("classroomCode"), Requester.of("requester"));

        // then
        assertNotNull(elrolmentModel);
    }

    @Test
    void 수강신청_철회() {
        // given
        newElrolment(aElrolment().code(ClassroomCode.of("cencel")).requesterInfo(aRequester().requester("requester").build()));

        assertDoesNotThrow(()->{
            elrolmentService.cencel(ClassroomCode.of("cencel"), Requester.of("requester"));
        });
    }
}
