package com.ljy.videoclass.classroom.command.infrastructure;

import com.ljy.videoclass.classroom.command.application.ElrolmentRepository;
import com.ljy.videoclass.classroom.domain.read.ElrolmentModel;
import com.ljy.videoclass.elrolment.domain.QElrolmentUser;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.Requester;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class QuerydslClassroomElrolmentRepository implements ElrolmentRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<ElrolmentModel> findByClassroomCodeAndRequester(String classroomCode, String requester) {
        ElrolmentModel elrolmentUser = jpaQueryFactory.select(Projections.constructor(ElrolmentModel.class,
                    QElrolmentUser.elrolmentUser.code,
                    QElrolmentUser.elrolmentUser.requesterInfo().requester,
                    QElrolmentUser.elrolmentUser.state,
                    QElrolmentUser.elrolmentUser.elrolmentDate
                ))
                .from(QElrolmentUser.elrolmentUser)
                .where(QElrolmentUser.elrolmentUser.code.eq(ClassroomCode.of(classroomCode)).and(QElrolmentUser.elrolmentUser.requesterInfo().requester.eq(requester)))
                .fetchFirst();
        return Optional.ofNullable(elrolmentUser);
    }
}
