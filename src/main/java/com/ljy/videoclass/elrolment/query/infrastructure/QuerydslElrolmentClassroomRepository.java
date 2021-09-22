package com.ljy.videoclass.elrolment.query.infrastructure;

import com.ljy.videoclass.classroom.domain.QClassroom;
import com.ljy.videoclass.elrolment.query.application.ClassroomRepository;
import com.ljy.videoclass.elrolment.query.application.model.ClassroomModel;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ljy.videoclass.classroom.domain.QClassroom.classroom;
import static com.querydsl.core.types.dsl.Expressions.asSimple;

@Repository
public class QuerydslElrolmentClassroomRepository implements ClassroomRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<ClassroomModel> findByCode(String classroomCode) {
        return Optional.ofNullable(jpaQueryFactory.select(Projections.constructor(ClassroomModel.class,
                        asSimple(classroomCode),
                        classroom.register().id
                    ))
                .from(classroom)
                .where(classroom.code().code.eq(classroomCode))
                .fetchFirst());
    }
}
