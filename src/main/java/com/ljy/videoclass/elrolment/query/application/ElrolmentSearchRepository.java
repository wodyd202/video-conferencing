package com.ljy.videoclass.elrolment.query.application;

import com.ljy.videoclass.classroom.command.application.model.ElrolmentModel;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;

import java.util.List;

public interface ElrolmentSearchRepository {
    List<ElrolmentModel> findByCode(ClassroomCode classroomCode);
}
