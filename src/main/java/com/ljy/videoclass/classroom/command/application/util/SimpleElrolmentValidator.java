package com.ljy.videoclass.classroom.command.application.util;

import com.ljy.videoclass.classroom.command.application.ElrolmentRepository;
import com.ljy.videoclass.classroom.domain.ElrolmentValidator;
import com.ljy.videoclass.classroom.domain.exception.AlreadyDisabledClassException;
import com.ljy.videoclass.classroom.domain.exception.AlreadyElrolmentException;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.ClassroomState;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.classroom.domain.value.Requester;
import com.ljy.videoclass.elrolment.domain.exception.InvalidElrolmentException;
import org.springframework.stereotype.Component;

@Component
public class SimpleElrolmentValidator implements ElrolmentValidator {
    private final ElrolmentRepository elrolmentRepository;

    public SimpleElrolmentValidator(ElrolmentRepository elrolmentRepository) {
        this.elrolmentRepository = elrolmentRepository;
    }

    @Override
    public void validation(ClassroomCode classroomCode, Register register, Requester requester, ClassroomState state) {
        if(isDisable(state)){
            throw new AlreadyDisabledClassException("비활성화되어있는 수업에 수강신청할 수 없습니다.");
        }
        if(eqRegister(register, requester)){
            throw new InvalidElrolmentException("자신의 수업에 수강신청을 진행할 수 없습니다.");
        }

        if(elrolmentRepository.findByClassroomCodeAndRequester(classroomCode.get(), requester.get()).isPresent()){
            throw new AlreadyElrolmentException();
        }
    }

    private boolean isDisable(ClassroomState state) {
        return state.equals(ClassroomState.Disable);
    }

    private boolean eqRegister(Register register, Requester requester) {
        return register.equals(Register.of(requester.get()));
    }
}
