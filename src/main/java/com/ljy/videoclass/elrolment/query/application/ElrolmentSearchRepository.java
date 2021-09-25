package com.ljy.videoclass.elrolment.query.application;

import com.ljy.videoclass.classroom.domain.read.ElrolmentModel;
import com.ljy.videoclass.core.http.PageRequest;
import com.ljy.videoclass.elrolment.domain.read.ElrolmentUserModel;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.ElrolmentState;

import java.util.List;

public interface ElrolmentSearchRepository {
    List<ElrolmentUserModel> findByCodeAndState(ClassroomCode classroomCode, ElrolmentState state);

    List<ElrolmentModel> findByRegisterAndState(String requester, ElrolmentState state, PageRequest pageRequest);

    long countByRegisterAndState(String requester, ElrolmentState state);
}
