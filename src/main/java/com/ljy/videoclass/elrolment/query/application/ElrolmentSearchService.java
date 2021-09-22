package com.ljy.videoclass.elrolment.query.application;

import com.ljy.videoclass.classroom.command.application.model.ElrolmentModel;
import com.ljy.videoclass.elrolment.domain.exception.ClassroomNotfoundException;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.query.application.model.ClassroomModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElrolmentSearchService {
    private final ElrolmentSearchRepository elrolmentSearchRepository;
    private final ClassroomRepository classroomRepository;

    public ElrolmentSearchService(ElrolmentSearchRepository elrolmentSearchRepository, ClassroomRepository classroomRepository) {
        this.elrolmentSearchRepository = elrolmentSearchRepository;
        this.classroomRepository = classroomRepository;
    }

    public List<ElrolmentModel> findByCodeAndRegister(ClassroomCode classroomCode, String register) {
        ClassroomModel classroomModel = classroomRepository.findByCode(classroomCode.get()).orElseThrow(ClassroomNotfoundException::new);
        if(!classroomModel.equalsRegister(register)){
            throw new ClassroomNotfoundException();
        }
        return elrolmentSearchRepository.findByCode(classroomCode);
    }
}
