package com.ljy.videoclass.services.elrolment.query.application;

import com.ljy.videoclass.core.http.PageRequest;
import com.ljy.videoclass.services.elrolment.command.application.external.Classroom;
import com.ljy.videoclass.services.elrolment.command.application.external.ExternalClassroomRepository;
import com.ljy.videoclass.services.elrolment.domain.exception.ClassroomNotfoundException;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.services.elrolment.query.application.model.ElrolmentModels;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ElrolmentSearchService {
    private ElrolmentSearchRepository elrolmentSearchRepository;
    private ExternalClassroomRepository classroomRepository;

    /**
     * @param classroomCode
     * @param register
     * @param state
     * # 자신의 수업 리스트 조회
     */
    public List<ElrolmentModel> findByCodeAndRegisterAndState(ClassroomCode classroomCode, String register, ElrolmentState state) {
        Classroom classroom = classroomRepository.getClassroom(classroomCode.get()).orElseThrow(ClassroomNotfoundException::new);
        return elrolmentSearchRepository.findByCodeAndState(classroomCode, state);
    }

    /**
     * @param requester
     * @param state
     * @param pageRequest
     * @return
     */
    public ElrolmentModels findByRegisterAndState(String requester, ElrolmentState state, PageRequest pageRequest) {
        return ElrolmentModels.builder()
                .elrolmentModels(elrolmentSearchRepository.findByRegisterAndState(requester,state, pageRequest))
                .totalElement(elrolmentSearchRepository.countByRegisterAndState(requester,state))
                .build();
    }
}
