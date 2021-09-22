package com.ljy.videoclass.elrolment.query.infrastructure;

import com.ljy.videoclass.classroom.command.application.model.ElrolmentModel;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.query.application.ElrolmentSearchRepository;
import com.querydsl.core.types.Projections;
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
    public List<ElrolmentModel> findByCode(ClassroomCode classroomCode) {
        return jpaQueryFactory.select(Projections.constructor(ElrolmentModel.class,
                        asSimple(classroomCode),
                        elrolmentUser.userId,
                        elrolmentUser.state,
                        elrolmentUser.elrolmentDate))
                .from(elrolmentUser)
                .fetch();
    }
}
