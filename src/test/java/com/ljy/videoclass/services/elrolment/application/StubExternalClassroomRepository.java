package com.ljy.videoclass.services.elrolment.application;

import com.ljy.videoclass.services.elrolment.command.application.external.Classroom;
import com.ljy.videoclass.services.elrolment.command.application.external.ExternalClassroomRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class StubExternalClassroomRepository implements ExternalClassroomRepository {
    private HashMap<String, Classroom> repo = new HashMap<>();

    @Override
    public Optional<Classroom> getClassroom(String classroomCode) {
        return Optional.ofNullable(repo.get(classroomCode));
    }

    public void save(Classroom classroom){
        repo.put(classroom.getClassroomCode(), classroom);
    }
}
