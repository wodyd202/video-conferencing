package com.ljy.videoclass.enrollment.command.infrastructure;

import com.ljy.videoclass.enrollment.domain.ClassroomModel;
import com.ljy.videoclass.enrollment.domain.ClassroomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ljy.videoclass.classroom.domain.QClassroom.classroom;
import static com.querydsl.core.types.dsl.Expressions.asSimple;

@Repository
public class QuerydslEnrollmentClassroomRepository implements ClassroomRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<ClassroomModel> findByCode(String code) {
        ClassroomModel classroomModel = jpaQueryFactory.select(Projections.constructor(ClassroomModel.class,
                        asSimple(code),
                        classroom.register().id))
                .from(classroom)
                .where(classroom.code().code.eq(code))
                .fetchFirst();
        return Optional.ofNullable(classroomModel);
    }
}
