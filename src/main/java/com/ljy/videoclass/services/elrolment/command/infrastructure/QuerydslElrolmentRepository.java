package com.ljy.videoclass.services.elrolment.command.infrastructure;

import com.ljy.videoclass.services.elrolment.command.application.ElrolmentRepository;
import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.Requester;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static com.ljy.videoclass.services.elrolment.domain.QElrolment.elrolment;

@Repository
@Transactional
public class QuerydslElrolmentRepository implements ElrolmentRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @PersistenceContext private EntityManager entityManager;

    @Override
    public void save(Elrolment elrolmentUser) {
        if(entityManager.contains(elrolmentUser)){
            entityManager.merge(elrolmentUser);
        }else{
            entityManager.persist(elrolmentUser);
        }
    }

    @Override
    public Optional<Elrolment> findByCodeAndRequesterInfo(ClassroomCode classroomCode, Requester requester) {
        return Optional.ofNullable(jpaQueryFactory.select(elrolment)
                .from(elrolment)
                .where(elrolment.classroomCode().eq(classroomCode).and(elrolment.requesterInfo().requester.eq(requester.get())))
                .fetchFirst());
    }

    @Override
    public void removeByCodeAndRequesterInfo(ClassroomCode classroomCode, Requester requester) {
        jpaQueryFactory.delete(elrolment)
                .where(elrolment.classroomCode().eq(classroomCode).and(elrolment.requesterInfo().requester.eq(requester.get())))
                .execute();
    }
}
