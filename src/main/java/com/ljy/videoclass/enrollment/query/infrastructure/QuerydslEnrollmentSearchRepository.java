package com.ljy.videoclass.enrollment.query.infrastructure;

import com.ljy.videoclass.enrollment.domain.QEnrollment;
import com.ljy.videoclass.enrollment.domain.read.EnrollmentModel;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.EnrollmentState;
import com.ljy.videoclass.enrollment.query.application.EnrollmentSearchRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ljy.videoclass.enrollment.domain.QEnrollment.enrollment;

@Repository
public class QuerydslEnrollmentSearchRepository implements EnrollmentSearchRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<EnrollmentModel> findByCodeAndState(ClassroomCode classroomCode, EnrollmentState state) {
        return jpaQueryFactory.select(Projections.constructor(EnrollmentModel.class,
                        enrollment.requester,
                        enrollment.classroomCode,
                        enrollment.state))
                .from(enrollment)
                .where(enrollment.classroomCode.eq(classroomCode).and(enrollment.state.eq(state)))
                .fetch();
    }
}
