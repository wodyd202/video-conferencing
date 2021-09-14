package com.ljy.videoclass.classroom.query.infrastructure;

import com.ljy.videoclass.classroom.domain.QClassroom;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.classroom.query.application.ClassroomSearchRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ljy.videoclass.classroom.domain.QClassroom.classroom;
import static com.querydsl.core.types.dsl.Expressions.asSimple;

@Repository
public class QuerydslClassroomSearchRepository implements ClassroomSearchRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ClassroomModel> findByRegister(String register) {
        return jpaQueryFactory.select(Projections.constructor(ClassroomModel.class,
                        classroom.code().code,
                        classroom.classInfo().color,
                        classroom.classInfo().title().title,
                        classroom.classDateInfo().dayOfWeek,
                        classroom.classDateInfo().startHour,
                        classroom.classDateInfo().endHour,
                        asSimple(register)))
                .from(classroom)
                .where(classroom.register().eq(Register.of(register))).fetch();
    }
}
