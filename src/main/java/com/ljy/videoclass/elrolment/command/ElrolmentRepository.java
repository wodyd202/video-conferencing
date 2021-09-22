package com.ljy.videoclass.elrolment.command;

import com.ljy.videoclass.elrolment.domain.ElrolmentUser;
import com.ljy.videoclass.classroom.domain.read.ErolmentUserModel;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.Requester;

import java.util.List;
import java.util.Optional;

public interface ElrolmentRepository {
    void save(ElrolmentUser elrolmentUser);

    Optional<ElrolmentUser> findByCodeAndRequester(ClassroomCode classroomCode, Requester requester);
}
