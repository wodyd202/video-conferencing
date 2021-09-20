package com.ljy.videoclass.elrolment;

import com.ljy.videoclass.classroom.command.application.SimpleElrolmentValidator;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.OpenClassroomValidator;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.elrolment.domain.exception.InvalidElrolmentException;
import com.ljy.videoclass.user.domain.value.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class Elrolment_Test {

    @Test
    @DisplayName("자신의 수업을 수강신청 할 수 없음")
    void elrolment_invalid(){
        OpenClassroom openClassroom = aOpenClassroom().build();
        Classroom classroom = openClassroom(openClassroom);

        assertThrows(InvalidElrolmentException.class,()->{
            classroom.elrolment(new SimpleElrolmentValidator(), Register.of("00000000"));
        });
    }

    private Classroom openClassroom(OpenClassroom openClassroom){
        Classroom classroom = Classroom.createWith(openClassroom, Register.of("00000000"));
        classroom.open(mock(OpenClassroomValidator.class));
        return classroom;
    }
}
