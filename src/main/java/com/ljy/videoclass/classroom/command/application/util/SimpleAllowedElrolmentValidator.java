package com.ljy.videoclass.classroom.command.application.util;

import com.ljy.videoclass.classroom.command.application.ElrolmentRepository;
import com.ljy.videoclass.classroom.domain.AllowElrolmentValidator;
import com.ljy.videoclass.classroom.domain.exception.AlreadyAllowedElrolmentException;
import com.ljy.videoclass.classroom.domain.exception.AlreadyDisabledClassException;
import com.ljy.videoclass.classroom.domain.exception.ElrolmentNotFoundException;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.ClassroomState;
import com.ljy.videoclass.classroom.domain.value.Requester;
import com.ljy.videoclass.classroom.domain.read.ElrolmentModel;
import org.springframework.stereotype.Component;

@Component
public class SimpleAllowedElrolmentValidator implements AllowElrolmentValidator {
    private final ElrolmentRepository elrolmentRepository;

    public SimpleAllowedElrolmentValidator(ElrolmentRepository elrolmentRepository) {
        this.elrolmentRepository = elrolmentRepository;
    }

    @Override
    public void validation(ClassroomCode code, Requester requester, ClassroomState state) {
        if(isDisabled(state)){
            throw new AlreadyDisabledClassException("비활성화되어있는 수업에 학생을 참여시킬 수 없습니다.");
        }
        ElrolmentModel elrolmentModel = elrolmentRepository.findByClassroomCodeAndRequester(code.get(), requester.get()).orElseThrow(ElrolmentNotFoundException::new);
        if(elrolmentModel.getState().equals("ALLOWED")){
            throw new AlreadyAllowedElrolmentException("이미 해당 수업에 참여하고 있는 학생을 수업에 참여시킬 수 없습니다.");
        }
    }

    private boolean isDisabled(ClassroomState state) {
        return state.equals(ClassroomState.Disable);
    }
}
