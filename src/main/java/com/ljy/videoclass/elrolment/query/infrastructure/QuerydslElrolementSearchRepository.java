package com.ljy.videoclass.elrolment.query.infrastructure;

import com.ljy.videoclass.classroom.domain.read.ElrolmentModel;
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
    public List<ElrolmentModel> findByCodeAndState(ClassroomCode classroomCode, ElrolmentState state) {
        return jpaQueryFactory.select(Projections.constructor(ElrolmentModel.class,
                        asSimple(classroomCode),
                        elrolmentUser.userId,
                        elrolmentUser.state,
                        elrolmentUser.elrolmentDate))
                .from(elrolmentUser)
                .where(eqCode(classroomCode), eqState(state))
                .fetch();
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
