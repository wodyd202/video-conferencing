package com.ljy.videoclass.services.elrolment.query.application;

import com.ljy.videoclass.core.http.PageRequest;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.ElrolmentState;

import java.util.List;

public interface ElrolmentSearchRepository {
    List<ElrolmentModel> findByCodeAndState(ClassroomCode classroomCode, ElrolmentState state);

    List<com.ljy.videoclass.services.classroom.domain.read.ElrolmentModel> findByRegisterAndState(String requester, ElrolmentState state, PageRequest pageRequest);

    long countByRegisterAndState(String requester, ElrolmentState state);
}
