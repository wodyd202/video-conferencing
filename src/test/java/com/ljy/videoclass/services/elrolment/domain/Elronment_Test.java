package com.ljy.videoclass.services.elrolment.domain;

import com.ljy.videoclass.services.classroom.ClassroomFixture;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import com.ljy.videoclass.services.elrolment.command.application.external.UserInfo;
import com.ljy.videoclass.services.elrolment.command.application.util.DefaultRequestElrolmentValidator;
import com.ljy.videoclass.services.elrolment.command.application.ElrolmentMapper;
import com.ljy.videoclass.services.elrolment.application.StubExternalClassroomRepository;
import com.ljy.videoclass.services.elrolment.command.application.external.Classroom;
import com.ljy.videoclass.services.elrolment.command.application.ElrolmentRepository;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentRequesterInfoModel;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.services.elrolment.domain.value.Requester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.ljy.videoclass.services.elrolment.ElrolmentFixture.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 수강신청 도메인 테스트
 */
public class Elronment_Test {
    ElrolmentMapper elrolmentFactory = new ElrolmentMapper();

    @Test
    void 수강신청_생성(){
        // given
        ClassroomModel classroomModel = ClassroomFixture.aClassroom(Register.of("생성자")).toModel();
        ClassroomCode classCode = ClassroomCode.of(classroomModel.getCode());
        UserInfo requesterInfo = UserInfo.builder()
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
        assertEquals(elrolmentModel.getClassroomCode(), classCode.get());
    }

    StubExternalClassroomRepository classroomRepository;
    ElrolmentRepository elrolmentRepository;
    RequestElrolmentValidator requestElrolmentValidator;

    @BeforeEach
    void setUp(){
        elrolmentRepository = new StubElrolmentRepository();
        classroomRepository = new StubExternalClassroomRepository();
        requestElrolmentValidator = new DefaultRequestElrolmentValidator(classroomRepository, elrolmentRepository);
    }

    @Test
    void 수강_신청(){
        // given
        Classroom classroom = aClassroom().classroomCode("classroomCode").build();
        classroomRepository.save(classroom);
        Elrolment elrolment = aElrolment().code(ClassroomCode.of("classroomCode")).build();

        // when
        elrolment.request(requestElrolmentValidator);
        ElrolmentModel elrolmentModel = elrolment.toModel();

        // then
        assertEquals(elrolmentModel.getState(), ElrolmentState.NOT);
    }

    @Test
    void 자신의_강의를_수강신청하면_에러발생(){
        // given
        Classroom classroom = aClassroom().owner("owner").classroomCode("classroomCode").build();
        classroomRepository.save(classroom);
        Elrolment elrolment = aElrolment().code(ClassroomCode.of("classroomCode"))
                                          .requesterInfo(aRequester().requester("owner").build())
                                .build();

        // when
        assertThrows(IllegalArgumentException.class, ()->{
            elrolment.request(requestElrolmentValidator);
        });
    }

    @Test
    void 수업_개설자와_수강신청자가_같은사람임(){
        Classroom classroom = aClassroom().owner("equal").build();

        assertTrue(classroom.equalsOnwer(Requester.of("equal")));
    }

    @Test
    void 수업_개설자와_수강신청자가_다른사람임(){
        Classroom classroom = aClassroom().owner("equal").build();

        assertFalse(classroom.equalsOnwer(com.ljy.videoclass.services.elrolment.domain.value.Requester.of("notEqual")));
    }

    @Test
    void 이미_지난_강의(){
        // when
        Classroom classroom = aClassroom().classOptionalDateInfo(new Classroom.ClassOptionalDateInfo(LocalDate.now().minusDays(1))).build();

        // then
        assertTrue(classroom.isFinished());
    }

    @Test
    void 아직_기한이_남은_강의(){
        // when
        Classroom classroom = aClassroom().classOptionalDateInfo(new Classroom.ClassOptionalDateInfo(LocalDate.now().plusDays(1))).build();

        // then
        assertFalse(classroom.isFinished());
    }

    @Test
    void 이미_지난_강의를_수강신청한경우_에러발생(){
        // given
        Classroom classroom = aClassroom().classOptionalDateInfo(new Classroom.ClassOptionalDateInfo(LocalDate.now().minusDays(1)))
                                          .owner("owner")
                                          .classroomCode("classroomCode").build();
        classroomRepository.save(classroom);
        Elrolment elrolment = aElrolment().code(ClassroomCode.of("classroomCode"))
                                        .requesterInfo(aRequester().requester("requester").build())
                                        .build();

        // when
        assertThrows(IllegalStateException.class, ()->{
            elrolment.request(requestElrolmentValidator);
        });
    }

    @Test
    void 비활성화된_강의(){
        // when
        Classroom classroom = aClassroom().state(Classroom.ClassroomState.Disable).build();

        // then
        assertTrue(classroom.isDisabled());
    }

    @Test
    void 활성화된_강의(){
        // when
        Classroom classroom = aClassroom().state(Classroom.ClassroomState.Activate).build();

        // then
        assertFalse(classroom.isDisabled());
    }

    @Test
    void 비활성화된_강의를_수강신청한경우_에러발생(){
        // given
        Classroom classroom = aClassroom().state(Classroom.ClassroomState.Disable)
                                          .classroomCode("classroomCode").build();
        classroomRepository.save(classroom);
        Elrolment elrolment = aElrolment().code(ClassroomCode.of("classroomCode"))
                .requesterInfo(aRequester().requester("requester").build())
                .build();

        // when
        assertThrows(IllegalStateException.class, ()->{
            elrolment.request(requestElrolmentValidator);
        });
    }

    @Test
    void 이미_해당_강의를_수강신청한경우_에러발생(){
        // given
        Classroom classroom = aClassroom().classroomCode("classroomCode").build();
        classroomRepository.save(classroom);
        Elrolment elrolment = aElrolment().requesterInfo(aRequester().requester("requester").build()).code(ClassroomCode.of("classroomCode")).build();
        elrolmentRepository.save(elrolment);

        // when
        Elrolment requestElorlment = aElrolment().requesterInfo(aRequester().requester("requester").build()).code(ClassroomCode.of("classroomCode")).build();
        assertThrows(IllegalArgumentException.class, ()->{
            requestElorlment.request(requestElrolmentValidator);
        });
    }

    @Test
    void 수강신청_수락() {
        // given
        Elrolment elrolment = aElrolment().build();

        // when
        elrolment.allow();
        ElrolmentModel elrolmentModel = elrolment.toModel();

        // then
        assertEquals(elrolmentModel.getState(), ElrolmentState.ALLOWED);
    }
}
