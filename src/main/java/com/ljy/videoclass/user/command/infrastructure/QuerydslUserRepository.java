package com.ljy.videoclass.user.command.infrastructure;

import com.ljy.videoclass.user.domain.User;
import com.ljy.videoclass.user.domain.value.UserId;
import com.ljy.videoclass.user.command.application.UserRepository;
import com.ljy.videoclass.user.domain.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class QuerydslUserRepository implements UserRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Autowired private EntityManager entityManager;

    @Override
    public boolean existByUserId(UserId userId) {
        Integer integer = jpaQueryFactory.selectOne()
                .from(QUser.user)
                .where(QUser.user.id().eq(userId))
                .fetchFirst();
        return integer != null && integer > 0;
    }

    @Override
    public void save(User user) {
        if(entityManager.contains(user)){
            entityManager.merge(user);
        }else{
            entityManager.persist(user);
        }
    }
}
