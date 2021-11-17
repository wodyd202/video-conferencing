package com.ljy.videoclass.services.elrolment.query.infrastructure;

import com.ljy.videoclass.services.elrolment.command.application.external.Classroom;
import com.ljy.videoclass.services.elrolment.command.application.external.ExternalClassroomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.asSimple;

@Profile("!test")
@Repository
public class QuerydslElrolmentClassroomRepository implements ExternalClassroomRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Classroom> getClassroom(String classroomCode) {
        return null;
    }
}
