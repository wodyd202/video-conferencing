package com.ljy.videoclass.enrollment.query.application;

import com.ljy.videoclass.enrollment.domain.ClassroomModel;
import com.ljy.videoclass.enrollment.domain.ClassroomRepository;
import com.ljy.videoclass.enrollment.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.enrollment.domain.read.EnrollmentModel;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.EnrollmentState;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentSearchService {
    private final ClassroomRepository classroomRepository;
    private final EnrollmentSearchRepository enrollmentRepository;

    public EnrollmentSearchService(ClassroomRepository classroomRepository, EnrollmentSearchRepository enrollmentRepository) {
        this.classroomRepository = classroomRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<EnrollmentModel> findByCodeAndRegister(ClassroomCode classroomCode, EnrollmentState state, String register){
        ClassroomModel classroomModel = classroomRepository.findByCode(classroomCode.get()).orElseThrow(ClassroomNotFoundException::new);
        if(!classroomModel.getRegister().equals(register)){
            throw new ClassroomNotFoundException();
        }
        return enrollmentRepository.findByCodeAndState(classroomCode, state);
    }

}
