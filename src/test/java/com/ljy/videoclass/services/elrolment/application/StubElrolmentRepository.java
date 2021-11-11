package com.ljy.videoclass.services.elrolment.application;

import com.ljy.videoclass.services.elrolment.command.application.ElrolmentRepository;
import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.Requester;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StubElrolmentRepository implements ElrolmentRepository {

    @Override
    public void save(Elrolment elrolmentUser) {

    }

    @Override
    public Optional<Elrolment> findByCodeAndRequester(ClassroomCode classroomCode, Requester requester) {
        return Optional.empty();
    }
}
