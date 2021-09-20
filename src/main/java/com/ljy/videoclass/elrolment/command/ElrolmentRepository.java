package com.ljy.videoclass.elrolment.command;

import com.ljy.videoclass.elrolment.domain.ElrolmentUser;
import com.ljy.videoclass.classroom.domain.read.ErolmentUserModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;

import java.util.List;

public interface ElrolmentRepository {
    void save(ElrolmentUser elrolmentUser);
    List<ErolmentUserModel> findByClassroomCode(ClassroomCode code);
}
