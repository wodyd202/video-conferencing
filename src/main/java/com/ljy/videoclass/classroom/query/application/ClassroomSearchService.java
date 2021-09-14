package com.ljy.videoclass.classroom.query.application;

import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomSearchService {
    private final ClassroomSearchRepository classroomRepository;

    public ClassroomSearchService(ClassroomSearchRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public List<ClassroomModel> findByRegister(String register) {
        return classroomRepository.findByRegister(register);
    }
}
