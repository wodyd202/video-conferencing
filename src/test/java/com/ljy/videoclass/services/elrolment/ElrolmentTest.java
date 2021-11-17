package com.ljy.videoclass.services.elrolment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.services.elrolment.command.application.external.Classroom;
import com.ljy.videoclass.services.elrolment.application.StubExternalClassroomRepository;
import com.ljy.videoclass.services.elrolment.application.StubExternalUserRepository;
import com.ljy.videoclass.services.elrolment.command.application.ElrolmentRepository;
import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.RequestElrolmentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;

@SpringBootTest
@AutoConfigureMockMvc
public class ElrolmentTest {
    @Autowired
    protected ElrolmentRepository elrolmentRepository;

    @Autowired
    protected StubExternalClassroomRepository externalClassroomRepository;

    @Autowired
    protected StubExternalUserRepository externalUserRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    protected void newUser(String userId){
        externalUserRepository.save(userId);
    }

    protected void newClassroom(Classroom.ClassroomBuilder classroomBuilder){
        Classroom classroom = classroomBuilder.build();
        externalClassroomRepository.save(classroom);
    }

    protected void newElrolment(Elrolment.ElrolmentBuilder elrolmentBuilder){
        Elrolment elrolment = elrolmentBuilder.build();
        elrolment.request(mock(RequestElrolmentValidator.class));
        elrolmentRepository.save(elrolment);
    }
}
