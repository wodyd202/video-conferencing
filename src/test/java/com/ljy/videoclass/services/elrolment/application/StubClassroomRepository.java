package com.ljy.videoclass.services.elrolment.application;

import com.ljy.videoclass.services.elrolment.query.application.ClassroomRepository;
import com.ljy.videoclass.services.elrolment.query.application.model.ClassroomModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StubClassroomRepository implements ClassroomRepository {

    @Override
    public Optional<ClassroomModel> findByCode(String classroomCode) {
        return Optional.empty();
    }
}
