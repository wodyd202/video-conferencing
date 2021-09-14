package com.ljy.videoclass.enrollment.query.application;

import com.ljy.videoclass.enrollment.domain.read.EnrollmentModel;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.EnrollmentState;

import java.util.List;

public interface EnrollmentSearchRepository {
    List<EnrollmentModel> findByCodeAndState(ClassroomCode classroomCode, EnrollmentState state);
}
