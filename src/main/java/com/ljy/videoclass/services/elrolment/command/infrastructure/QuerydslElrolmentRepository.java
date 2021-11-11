package com.ljy.videoclass.services.elrolment.command.infrastructure;

import com.ljy.videoclass.services.elrolment.command.application.ElrolmentRepository;
import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.Requester;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static com.ljy.videoclass.services.elrolment.domain.QElrolmentUser.elrolmentUser;

@Repository
@Primary
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
    public Optional<Elrolment> findByCodeAndRequester(ClassroomCode classroomCode, Requester requester) {
        return Optional.ofNullable(jpaQueryFactory.select(elrolmentUser)
                .from(elrolmentUser)
                .where(elrolmentUser.code.eq(classroomCode).and(elrolmentUser.requesterInfo().requester.eq(requester.get())))
                .fetchFirst());
    }
}
