package com.ljy.user.command.infrastructure;

import com.ljy.user.command.domain.User;
import com.ljy.user.command.domain.UserId;
import com.ljy.user.command.domain.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.ljy.user.command.domain.QUser.user;

@Repository
@Transactional
public class QuerydslUserRepository implements UserRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Autowired private EntityManager entityManager;

    @Override
    public boolean existByUserId(UserId userId) {
        Integer integer = jpaQueryFactory.selectOne()
                .from(user)
                .where(user.id().eq(userId))
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
