package com.ljy.videoclass.enrollment.command.infrastructure;

import com.ljy.videoclass.enrollment.command.application.EnrollmentRepository;
import com.ljy.videoclass.enrollment.domain.Enrollment;
import com.ljy.videoclass.enrollment.domain.QEnrollment;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.Requester;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Transactional
public class QuerydslEnrollmentRepository implements EnrollmentRepository {
    @PersistenceContext private EntityManager entityManager;
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Enrollment> findByRequesterAndCode(Requester requester, ClassroomCode classroomCode) {
        Enrollment enrollment = jpaQueryFactory.select(QEnrollment.enrollment)
                .from(QEnrollment.enrollment)
                .where(QEnrollment.enrollment.requester.eq(requester).and(QEnrollment.enrollment.classroomCode.eq(classroomCode))).fetchFirst();
        return Optional.ofNullable(enrollment);
    }

    @Override
    public void save(Enrollment enrollment) {
        if(entityManager.contains(enrollment)){
            entityManager.merge(enrollment);
        }else{
            entityManager.persist(enrollment);
        }
    }
}
