package com.ljy.videoclass.services.elrolment.presentation;

import com.ljy.videoclass.services.elrolment.ElrolmentFixture;
import com.ljy.videoclass.services.elrolment.ElrolmentTest;
import com.ljy.videoclass.services.elrolment.command.application.external.Classroom;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static com.ljy.videoclass.services.elrolment.ElrolmentFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "000-0000-0000")
public class ElrolmentAPI_Test extends ElrolmentTest {

    @BeforeEach
    void setUp(){
        newUser("000-0000-0000");
    }

    @Test
    void 이미_수강신청한_시용자일_경우_에러발생() throws Exception {
        // given
        newClassroom(aClassroom().classroomCode("classroomCode"));
        newElrolment(aElrolment().code(ClassroomCode.of("classroomCode")).requesterInfo(aRequester().requester("000-0000-0000").build()));

        // when
        mockMvc.perform(post("/api/elrolment/{classroomCode}", "classroomCode"))

        // then
        .andExpect(status().isBadRequest());
    }

    @Test
    void 수강신청() throws Exception{
        // given
        newClassroom(aClassroom().classroomCode("newClassroomCode"));

        // when
        mockMvc.perform(post("/api/elrolment/{classroomCode}", "newClassroomCode"))

        // then
        .andExpect(status().isOk());
    }

    @Test
    void 수강신청_취소() throws Exception {
        // given
        newElrolment(aElrolment().code(ClassroomCode.of("removeElrolemnt")).requesterInfo(aRequester().requester("000-0000-0000").build()));

        // when
        mockMvc.perform(delete("/api/elrolment/{classroomCode}", "removeElrolemnt"))

        // then
        .andExpect(status().isOk());
    }
}
