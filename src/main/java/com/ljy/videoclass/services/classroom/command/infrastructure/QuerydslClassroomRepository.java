package com.ljy.videoclass.services.classroom.command.infrastructure;

import com.ljy.videoclass.services.classroom.command.application.ClassroomRepository;
import com.ljy.videoclass.services.classroom.domain.Classroom;
import com.ljy.videoclass.services.classroom.domain.QClassroom;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.asSimple;

@Repository
@Transactional
public class QuerydslClassroomRepository implements ClassroomRepository {
    @PersistenceContext private EntityManager entityManager;
    @Autowired private JPAQueryFactory jpaQueryFactory;

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