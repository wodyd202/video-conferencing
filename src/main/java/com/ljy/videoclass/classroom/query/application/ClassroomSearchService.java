package com.ljy.videoclass.classroom.query.application;

import com.ljy.videoclass.classroom.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomState;
import com.ljy.videoclass.classroom.query.application.model.ClassroomModels;
import com.ljy.videoclass.classroom.query.application.model.ClassroomSearchModel;
import com.ljy.videoclass.core.http.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ClassroomSearchService {
    private final ClassroomSearchRepository classroomRepository;

    public ClassroomSearchService(ClassroomSearchRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public ClassroomModels findByRegister(ClassroomState state, String register, PageRequest pageRequest) {
        return ClassroomModels.builder()
                .classrooms(classroomRepository.findByRegister(state, register, pageRequest))
                .totalElement(classroomRepository.countByRegister(state ,register))
                .build();
    }

    public ClassroomModels findByClassDateAndDayOfWeek(String register, ClassroomSearchModel classroomSearchModel, PageRequest pageRequest) {
        return ClassroomModels.builder()
                .classrooms(classroomRepository.findByClassDateAndDayOfWeek(classroomSearchModel,pageRequest,register))
                .totalElement(classroomRepository.countByClassDateAndDayOfWeek(classroomSearchModel,register))
                .build();
    }

    public ClassroomModels findLastClassroomByRegister(String register, PageRequest pageRequest) {
        return ClassroomModels.builder()
                .classrooms(classroomRepository.findLastClassroomByRegister(pageRequest,register))
                .totalElement(classroomRepository.countLastClassroomByRegister(register))
                .build();
    }

    public ClassroomModel findByCodeAndRegister(String classroomCode, String register) {
        return classroomRepository.findbyCodeAndRegister(classroomCode, register).orElseThrow(ClassroomNotFoundException::new);
    }
}
