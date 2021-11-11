package com.ljy.videoclass.services.elrolment.query.application;

import com.ljy.videoclass.core.http.PageRequest;
import com.ljy.videoclass.services.elrolment.domain.exception.ClassroomNotfoundException;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.services.elrolment.query.application.model.ClassroomModel;
import com.ljy.videoclass.services.elrolment.query.application.model.ElrolmentModels;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElrolmentSearchService {
    private final ElrolmentSearchRepository elrolmentSearchRepository;
    private final ClassroomRepository classroomRepository;

    public ElrolmentSearchService(ElrolmentSearchRepository elrolmentSearchRepository, ClassroomRepository classroomRepository) {
        this.elrolmentSearchRepository = elrolmentSearchRepository;
        this.classroomRepository = classroomRepository;
    }

    public List<ElrolmentModel> findByCodeAndRegisterAndState(ClassroomCode classroomCode, String register, ElrolmentState state) {
        ClassroomModel classroomModel = classroomRepository.findByCode(classroomCode.get()).orElseThrow(ClassroomNotfoundException::new);
        if(!classroomModel.equalsRegister(register)){
            throw new ClassroomNotfoundException();
        }
        return elrolmentSearchRepository.findByCodeAndState(classroomCode, state);
    }

    public ElrolmentModels findByRegisterAndState(String requester, ElrolmentState state, PageRequest pageRequest) {
        return ElrolmentModels.builder()
                .elrolmentModels(elrolmentSearchRepository.findByRegisterAndState(requester,state, pageRequest))
                .totalElement(elrolmentSearchRepository.countByRegisterAndState(requester,state))
                .build();
    }
}