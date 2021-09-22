package com.ljy.videoclass.elrolment;

import com.ljy.videoclass.classroom.command.application.ElrolmentRepository;
import com.ljy.videoclass.classroom.command.application.util.SimpleAllowedElrolmentValidator;
import com.ljy.videoclass.classroom.command.application.util.SimpleElrolmentValidator;
import com.ljy.videoclass.classroom.domain.AllowElrolmentValidator;
import com.ljy.videoclass.classroom.domain.exception.*;
import com.ljy.videoclass.classroom.domain.read.ElrolmentModel;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.OpenClassroomValidator;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.classroom.domain.value.Requester;
import com.ljy.videoclass.elrolment.domain.exception.InvalidElrolmentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.ljy.videoclass.classroom.ClassroomFixture.aOpenClassroom;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Elrolment_Test {

    OpenClassroom openClassroom = aOpenClassroom().build();
    Classroom classroom = openClassroom(openClassroom);

    ElrolmentRepository elrolmentRepository = mock(ElrolmentRepository.class);
    SimpleElrolmentValidator elrolmentValidator = new SimpleElrolmentValidator(elrolmentRepository);

    @BeforeEach
    void setUp() {
        try{
            classroom.active();
        }catch (AlreadyActiveClassException e){
        }
    }

    @Test
    @DisplayName("자신의 수업을 수강신청 할 수 없음")
    void elrolment_invalid(){
        assertThrows(InvalidElrolmentException.class,()->{
            classroom.elrolment(elrolmentValidator, Requester.of("00000000"));
        });
    }

    @Test
    @DisplayName("이미 수강 신청한 수업")
    void elrolment_alreadyElrolment(){
        ClassroomModel classroomModel = classroom.toModel();
        when(elrolmentRepository.findByClassroomCodeAndRequester(classroomModel.getCode(), "alreadyElrolment"))
                .thenReturn(Optional.of(mock(ElrolmentModel.class)));
        assertThrows(AlreadyElrolmentException.class,()->{
            classroom.elrolment(elrolmentValidator, Requester.of("alreadyElrolment"));
        });
    }

    @Test
    @DisplayName("비활성화된 수업에 수강신청 할 수 없음")
    void elrolment_disabled(){
        classroom.disable();

        assertThrows(AlreadyDisabledClassException.class,()->{
            classroom.elrolment(elrolmentValidator, Requester.of("disabled"));
        });
    }

    @Test
    @DisplayName("수강신청 수락")
    void elrolment_allowed() {
        ElrolmentModel elrolmentModel = mock(ElrolmentModel.class);
        when(elrolmentModel.getState())
                .thenReturn("NOT");

        when(elrolmentRepository.findByClassroomCodeAndRequester(classroom.toModel().getCode(),"requester"))
                .thenReturn(Optional.of(elrolmentModel));
        AllowElrolmentValidator allowedElrolmentValidator = new SimpleAllowedElrolmentValidator(elrolmentRepository);
        assertDoesNotThrow(()->{
            classroom.allowedElrolment(allowedElrolmentValidator, Requester.of("requester"));
        });
    }

    @Test
    @DisplayName("이미 수강중인 학생의 수강신청을 다시 수락할 수 없음")
    void elrolment_already_allowed(){
        ElrolmentModel elrolmentModel = mock(ElrolmentModel.class);
        when(elrolmentModel.getState())
                .thenReturn("ALLOWED");

        when(elrolmentRepository.findByClassroomCodeAndRequester(classroom.toModel().getCode(),"requester"))
                .thenReturn(Optional.of(elrolmentModel));
        AllowElrolmentValidator allowedElrolmentValidator = new SimpleAllowedElrolmentValidator(elrolmentRepository);

        assertThrows(AlreadyAllowedElrolmentException.class, ()->{
            classroom.allowedElrolment(allowedElrolmentValidator, Requester.of("requester"));
        });
    }

    @Test
    @DisplayName("해당 수강신청이 존재하지 않음")
    void elrolment_notfound(){
        when(elrolmentRepository.findByClassroomCodeAndRequester(classroom.toModel().getCode(),"requester"))
                .thenReturn(Optional.empty());
        AllowElrolmentValidator allowedElrolmentValidator = new SimpleAllowedElrolmentValidator(elrolmentRepository);

        assertThrows(ElrolmentNotFoundException.class, ()->{
            classroom.allowedElrolment(allowedElrolmentValidator, Requester.of("requester"));
        });
    }

    private Classroom openClassroom(OpenClassroom openClassroom){
        Classroom classroom = Classroom.createWith(openClassroom, Register.of("00000000"));
        classroom.open(mock(OpenClassroomValidator.class));
        return classroom;
    }
}
