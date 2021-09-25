package com.ljy.videoclass.elrolment.query.infrastructure;

import com.ljy.videoclass.classroom.domain.read.ElrolmentModel;
import com.ljy.videoclass.core.http.PageRequest;
import com.ljy.videoclass.elrolment.domain.read.ElrolmentUserModel;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.elrolment.query.application.ElrolmentSearchRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ljy.videoclass.elrolment.domain.QElrolmentUser.elrolmentUser;
import static com.querydsl.core.types.dsl.Expressions.asSimple;

@Repository
public class QuerydslElrolementSearchRepository implements ElrolmentSearchRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ElrolmentUserModel> findByCodeAndState(ClassroomCode classroomCode, ElrolmentState state) {
        return jpaQueryFactory.select(Projections.constructor(ElrolmentUserModel.class,
                        asSimple(classroomCode),
                        elrolmentUser.requesterInfo(),
                        asSimple(state),
                        elrolmentUser.elrolmentDate))
                .from(elrolmentUser)
                .where(eqCode(classroomCode), eqState(state))
                .fetch();
    }

    @Override
    public List<ElrolmentModel> findByRegisterAndState(String requester, ElrolmentState state, PageRequest pageRequest) {
        return jpaQueryFactory.select(Projections.constructor(ElrolmentModel.class,
                        elrolmentUser.code,
                        elrolmentUser.requesterInfo(),
                        asSimple(state),
                        elrolmentUser.elrolmentDate))
                .from(elrolmentUser)
                .where(eqRequester(requester), eqState(state))
                .limit(pageRequest.getSize())
                .offset(pageRequest.getSize() * pageRequest.getPage())
                .fetch();
    }

    @Override
    public long countByRegisterAndState(String requester, ElrolmentState state) {
        return jpaQueryFactory.selectOne()
                .from(elrolmentUser)
                .where(eqRequester(requester), eqState(state))
                .fetchCount();
    }

    private BooleanExpression eqRequester(String requester) {
        return elrolmentUser.requesterInfo().requester.eq(requester);
    }

    private BooleanExpression eqCode(ClassroomCode classroomCode){
        return elrolmentUser.code.eq(classroomCode);
    }

    private BooleanExpression eqState(ElrolmentState state){
        if(state == null){
            return null;
        }
        return elrolmentUser.state.eq(state);
    }
}
