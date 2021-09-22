package com.ljy.videoclass.elrolment.query.application;

import com.ljy.videoclass.classroom.domain.read.ElrolmentModel;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.ElrolmentState;

import java.util.List;

public interface ElrolmentSearchRepository {
    List<ElrolmentModel> findByCodeAndState(ClassroomCode classroomCode, ElrolmentState state);
}
