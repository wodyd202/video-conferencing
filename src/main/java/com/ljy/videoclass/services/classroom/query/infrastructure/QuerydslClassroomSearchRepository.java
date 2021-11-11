package com.ljy.videoclass.services.classroom.query.infrastructure;

import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomState;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import com.ljy.videoclass.services.classroom.query.application.ClassroomSearchRepository;
import com.ljy.videoclass.services.classroom.query.application.model.ClassroomSearchModel;
import com.ljy.videoclass.core.http.PageRequest;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ljy.videoclass.services.classroom.domain.QClassroom.classroom;
import static com.querydsl.core.types.dsl.Expressions.asSimple;

@Repository
public class QuerydslClassroomSearchRepository implements ClassroomSearchRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<ClassroomModel> findbyCodeAndRegister(String classroomCode, String register) {
        return Optional.ofNullable(jpaQueryFactory.select(classroomModel(register))
                                .from(classroom)
                                .where(eqRegister(register), eqCode(classroomCode))
                                .fetchFirst());
    }

    @Override
    public List<ClassroomModel> findLastClassroomByRegister(PageRequest pageRequest, String register) {
        return jpaQueryFactory.select(classroomModel(register))
                .from(classroom)
                .where(eqRegister(register).and(classroom.classOptionalDateInfo().endDate.lt(LocalDate.now())))
                .limit(pageRequest.getSize())
                .offset(pageRequest.getPage() * pageRequest.getSize()).fetch();
    }

    @Override
    public long countLastClassroomByRegister(String register) {
        return jpaQueryFactory.selectOne()
                .from(classroom)
                .where(eqRegister(register).and(classroom.classOptionalDateInfo().endDate.lt(LocalDate.now()))).fetchCount();
    }

    @Override
    public List<ClassroomModel> findByRegister(ClassroomState state, String register, PageRequest pageRequest) {
        return jpaQueryFactory.select(classroomModel(register))
                .from(classroom)
                .where(eqRegister(register), eqState(state))
                .limit(pageRequest.getSize())
                .offset(pageRequest.getPage() * pageRequest.getSize()).fetch();
    }

    @Override
    public long countByRegister(ClassroomState state, String register) {
        return jpaQueryFactory.selectOne()
                .from(classroom)
                .where(eqRegister(register), eqState(state))
                .fetchCount();
    }

    @Override
    public List<ClassroomModel> findByClassDateAndDayOfWeek(ClassroomSearchModel classroomSearchModel,PageRequest pageRequest, String register) {
        List<ClassroomModel> classroomModel = jpaQueryFactory.select(classroomModel(register))
                .from(classroom)
                .where(eqRegister(register).and(
                                        ltStartHour(classroomSearchModel.getEndHour()).and(
                                                        gteEndHour(classroomSearchModel.getEndHour()))
                                                .or(
                                                        lteStartHour(classroomSearchModel.getStartHour()).and(
                                                                gtEndHour(classroomSearchModel.getStartHour()))
                                                )
                                )
                                .and(eqDayOfWeek(classroomSearchModel.getDayOfWeek()).and(
                                                gteEndDate(classroomSearchModel.getEndDate()).and(
                                                        ltStartDate(classroomSearchModel.getEndDate())).or(
                                                        gtEndDate(classroomSearchModel.getStartDate()).and(
                                                                lteStartDate(classroomSearchModel.getStartDate()))
                                                )
                                        )
                                )
                )
                .limit(pageRequest.getSize())
                .offset(pageRequest.getPage() * pageRequest.getSize()).fetch();
        return classroomModel;
    }

    @Override
    public long countByClassDateAndDayOfWeek(ClassroomSearchModel classroomSearchModel, String register) {
        return jpaQueryFactory.selectOne()
                .from(classroom)
                .where(eqRegister(register).and(
                                        ltStartHour(classroomSearchModel.getEndHour()).and(
                                                        gteEndHour(classroomSearchModel.getEndHour()))
                                                .or(
                                                        lteStartHour(classroomSearchModel.getStartHour()).and(
                                                                gtEndHour(classroomSearchModel.getStartHour()))
                                                )
                                )
                                .and(eqDayOfWeek(classroomSearchModel.getDayOfWeek()).and(
                                                gteEndDate(classroomSearchModel.getEndDate()).and(
                                                        ltStartDate(classroomSearchModel.getEndDate())).or(
                                                        gtEndDate(classroomSearchModel.getStartDate()).and(
                                                                lteStartDate(classroomSearchModel.getStartDate()))
                                                )
                                        )
                                )
                ).fetchCount();
    }

    private BooleanExpression eqCode(String classroomCode) {return classroom.code().eq(ClassroomCode.of(classroomCode));}

    private BooleanExpression eqState(ClassroomState state){return classroom.state.eq(state);}

    private BooleanExpression eqRegister(String register){
        return classroom.register().eq(Register.of(register));
    }

    private BooleanExpression eqDayOfWeek(DayOfWeek dayOfWeek) {
        return classroom.classDateInfo().dayOfWeek.eq(dayOfWeek);
    }

    private BooleanExpression gteEndHour(int hour){
        return classroom.classDateInfo().endHour.goe(hour);
    }

    private BooleanExpression gtEndHour(int hour){return classroom.classDateInfo().endHour.gt(hour);}

    private BooleanExpression lteStartHour(int hour) {return classroom.classDateInfo().startHour.loe(hour);}

    private BooleanExpression ltStartHour(int hour){
        return classroom.classDateInfo().startHour.lt(hour);
    }

    private BooleanExpression lteStartDate(LocalDate localDate){
        return classroom.classOptionalDateInfo().startDate.loe(localDate);
    }

    private BooleanExpression ltStartDate(LocalDate localDate){
        return classroom.classOptionalDateInfo().startDate.lt(localDate);
    }

    private BooleanExpression gteEndDate(LocalDate localDate) {
        return classroom.classOptionalDateInfo().endDate.goe(localDate);
    }

    private BooleanExpression gtEndDate(LocalDate localDate) {
        return classroom.classOptionalDateInfo().endDate.gt(localDate);
    }

    private ConstructorExpression<ClassroomModel> classroomModel(String register) {
        return Projections.constructor(ClassroomModel.class,
                classroom.code().code,
                classroom.classInfo().color,
                classroom.classInfo().title().title,
//                classroom.classInfo().description().description,
                classroom.classDateInfo().dayOfWeek,
                classroom.classDateInfo().startHour,
                classroom.classDateInfo().endHour,
                classroom.classOptionalDateInfo(),
                register == null ? null : asSimple(register));
    }


}
