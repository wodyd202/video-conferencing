package com.ljy.videoclass.services.elrolment.command.application;

import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.Requester;

import java.util.Optional;

public interface ElrolmentRepository {
    void save(Elrolment elrolmentUser);

    Optional<Elrolment> findByCodeAndRequester(ClassroomCode classroomCode, Requester requester);
}
