package com.ljy.videoclass.classroom.command.infrastructure;

import com.ljy.videoclass.classroom.command.application.ClassroomRepository;
import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.QClassroom;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.ljy.videoclass.classroom.domain.QClassroom.classroom;
import static com.querydsl.core.types.dsl.Expressions.asSimple;

@Repository
@Transactional
public class QuerydslClassroomRepository implements ClassroomRepository {
    @PersistenceContext private EntityManager entityManager;
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Classroom> findClassDateInfoByRegister(Register register) {
        return jpaQueryFactory.select(Projections.constructor(Classroom.class,
                        classroom.classDateInfo(),
                        asSimple(register)))
                .from(classroom)
                .where(classroom.register().eq(register))
                .fetch();
    }

    @Override
    public Optional<Classroom> findByCodeAndRegister(ClassroomCode classroomCode, Register register) {
        Classroom classroom = jpaQueryFactory.select(QClassroom.classroom)
                .from(QClassroom.classroom)
                .where(QClassroom.classroom.code().eq(classroomCode).and(QClassroom.classroom.register().eq(register))).fetchFirst();
        return Optional.ofNullable(classroom);
    }

    @Override
    public Optional<Classroom> findByCode(ClassroomCode classroomCode) {
        Classroom classroom = jpaQueryFactory.select(QClassroom.classroom)
                .from(QClassroom.classroom)
                .where(QClassroom.classroom.code().eq(classroomCode)).fetchFirst();
        return Optional.ofNullable(classroom);
    }

    @Override
    public void save(Classroom classroom) {
        if(entityManager.contains(classroom)){
            entityManager.merge(classroom);
        }else{
            entityManager.persist(classroom);
        }
    }
}
