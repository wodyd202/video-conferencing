package com.ljy.videoclass.services.elrolment.command.application.util;

import com.ljy.videoclass.services.elrolment.command.application.external.Classroom;
import com.ljy.videoclass.services.elrolment.command.application.ElrolmentRepository;
import com.ljy.videoclass.services.elrolment.domain.RequestElrolmentValidator;
import com.ljy.videoclass.services.elrolment.domain.exception.ClassroomNotfoundException;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.Requester;
import com.ljy.videoclass.services.elrolment.domain.value.RequesterInfo;
import com.ljy.videoclass.services.elrolment.command.application.external.ExternalClassroomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class DefaultRequestElrolmentValidator implements RequestElrolmentValidator {
    private ExternalClassroomRepository classroomRepository;
    private ElrolmentRepository elrolmentRepository;

    @Override
    public void validation(ClassroomCode classroomCode, RequesterInfo requesterInfo) {
        Classroom classroom = classroomRepository.getClassroom(classroomCode.get()).orElseThrow(ClassroomNotfoundException::new);

        // 요청자
        Requester requester = Requester.of(requesterInfo.getRequester());

        if(classroom.equalsOnwer(requester)){
            throw new IllegalArgumentException("자신의 강의는 수강신청할 수 없습니다.");
        }
        if(classroom.isFinished()){
            throw new IllegalStateException("이미 지난강의는 수강신청할 수 없습니다.");
        }
        if(classroom.isDisabled()){
            throw new IllegalStateException("비활성화된 강의는 수강신청할 수 없습니다.");
        }
        boolean existElolrment = elrolmentRepository.findByCodeAndRequesterInfo(classroomCode, requester).isPresent();
        if(existElolrment){
            throw new IllegalArgumentException("이미 수강신청한 상태입니다.");
        }
    }
}
