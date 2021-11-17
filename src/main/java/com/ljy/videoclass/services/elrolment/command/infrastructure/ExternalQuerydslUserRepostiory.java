package com.ljy.videoclass.services.elrolment.command.infrastructure;

import com.ljy.videoclass.services.elrolment.command.application.external.ExternalUserRepository;
import com.ljy.videoclass.services.elrolment.command.application.external.UserInfo;
import com.ljy.videoclass.services.user.domain.QUser;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

import static com.ljy.videoclass.services.user.domain.QUser.user;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.core.types.dsl.Expressions.asSimple;

@Profile("!test")
@Repository
@Transactional(readOnly = true)
public class ExternalQuerydslUserRepostiory implements ExternalUserRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<UserInfo> getUser(String userId) {
        UserInfo userInfo = jpaQueryFactory.select(userInfo(userId))
                .from(user)
                .where(user.id().id.eq(userId))
                .fetchFirst();
        return Optional.ofNullable(userInfo);
    }

    private ConstructorExpression<UserInfo> userInfo(String userId) {
        return constructor(UserInfo.class,
                        asSimple(userId)
                        ,user.userInfo().image().path
                        ,user.userInfo().email().email
                        ,user.userInfo().username().name);
    }
}
