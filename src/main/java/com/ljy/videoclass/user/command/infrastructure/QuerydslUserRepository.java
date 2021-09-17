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
import java.util.Optional;

@Repository
@Transactional
public class QuerydslUserRepository implements UserRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Autowired private EntityManager entityManager;

    @Override
    public Optional<User> findById(UserId userId) {
        User user = jpaQueryFactory.select(QUser.user)
                .from(QUser.user)
                .where(QUser.user.id().eq(userId))
                .fetchFirst();
        return Optional.ofNullable(user);
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
