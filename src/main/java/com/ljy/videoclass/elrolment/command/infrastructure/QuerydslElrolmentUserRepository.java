package com.ljy.videoclass.elrolment.command.infrastructure;

import com.ljy.videoclass.elrolment.command.application.ElrolmentUserRepository;
import com.ljy.videoclass.elrolment.command.application.model.UserModel;
import com.ljy.videoclass.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ljy.videoclass.user.domain.QUser.user;
import static com.querydsl.core.types.Projections.constructor;

@Repository
@Transactional(readOnly = true)
public class QuerydslElrolmentUserRepository implements ElrolmentUserRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<UserModel> findById(String id) {
        return Optional.ofNullable(jpaQueryFactory.select(constructor(UserModel.class,
                        user.id().id,
                        user.userInfo().email,
                        user.userInfo().image,
                        user.userInfo().username))
                .from(user)
                .where(user.id().id.eq(id))
                .fetchFirst());
    }
}
