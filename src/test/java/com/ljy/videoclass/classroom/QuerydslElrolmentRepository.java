package com.ljy.videoclass.classroom;

import com.ljy.videoclass.elrolment.command.ElrolmentRepository;
import com.ljy.videoclass.elrolment.domain.ElrolmentUser;
import com.ljy.videoclass.elrolment.domain.QElrolmentUser;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.Requester;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static com.ljy.videoclass.elrolment.domain.QElrolmentUser.elrolmentUser;

@Repository
@Primary
public class QuerydslElrolmentRepository implements ElrolmentRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @PersistenceContext private EntityManager entityManager;

    @Override
    public void save(ElrolmentUser elrolmentUser) {
        if(entityManager.contains(elrolmentUser)){
            entityManager.merge(elrolmentUser);
        }else{
            entityManager.persist(elrolmentUser);
        }
    }

    @Override
    public Optional<ElrolmentUser> findByCodeAndRequester(ClassroomCode classroomCode, Requester requester) {
        return Optional.ofNullable(jpaQueryFactory.select(elrolmentUser)
                .from(elrolmentUser)
                .where(elrolmentUser.code.eq(classroomCode).and(elrolmentUser.userId.eq(requester)))
                .fetchFirst());
    }
}
