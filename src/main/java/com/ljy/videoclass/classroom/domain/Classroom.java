package com.ljy.videoclass.classroom.domain;

import com.ljy.videoclass.classroom.command.application.event.ElrolmentedEvent;
import com.ljy.videoclass.elrolment.command.ElrolmentRepository;
import com.ljy.videoclass.classroom.domain.exception.AlreadyActiveClassException;
import com.ljy.videoclass.classroom.domain.exception.AlreadyDisabledClassException;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.read.ErolmentUserModel;
import com.ljy.videoclass.classroom.domain.value.*;
import com.ljy.videoclass.elrolment.domain.exception.InvalidElrolmentException;
import com.ljy.videoclass.user.domain.value.UserId;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "class_room",indexes = {
        @Index(columnList = "register, dayOfWeek, startDate, endDate, startHour, endHour,code, title"),
        @Index(columnList = "register, state")
})
@DynamicUpdate
public class Classroom {
    @EmbeddedId
    @AttributeOverride(name = "code", column = @Column(name = "code", length = 50))
    private final ClassroomCode code;

    @Embedded
    private ClassInfo classInfo;

    @Embedded
    private ClassDateInfo classDateInfo;

    @Embedded
    @AttributeOverride(name = "autoEnabled", column = @Column(name = "autoEnabledState"))
    private ClassOptionalDateInfo classOptionalDateInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ClassroomState state;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "register", nullable = false, length = 30))
    private final Register register;
    private final LocalDateTime createDateTime;

    protected Classroom(){code = null; register = null; createDateTime = null;}

    public Classroom(ClassDateInfo classDateInfo, Register register){
        code = null;
        this.register = register;
        this.classDateInfo = classDateInfo;
        createDateTime = null;
    }

    /**
     * @param openClassroom 강의 생성 dto
     * @param register 강의 생성자
     */
    private Classroom(OpenClassroom openClassroom, Register register) {
        code = ClassroomCode.create();
        ChangeClassInfo classInfo = openClassroom.getClassInfo();
        ChangeClassDateInfo classDateInfo = openClassroom.getClassDateInfo();
        ChangeClassOptionalDateInfo classOptionalDateInfo = openClassroom.getClassOptionalDateInfo();        this.classDateInfo = new ClassDateInfo(classDateInfo);

        if(existChangeClassOptionalDateInfo(classOptionalDateInfo)){
            this.classOptionalDateInfo = mapFrom(classOptionalDateInfo);
        }
        this.classInfo = new ClassInfo(classInfo);
        this.register = register;
        createDateTime = LocalDateTime.now();
    }

    private boolean existChangeClassOptionalDateInfo(ChangeClassOptionalDateInfo classOptionalDateInfo) {
        return classOptionalDateInfo != null && (classOptionalDateInfo.getEndDate() != null || classOptionalDateInfo.getStartDate() != null);
    }

    private ClassOptionalDateInfo mapFrom(ChangeClassOptionalDateInfo classOptionalDateInfo){
        return ClassOptionalDateInfo.builder()
                .startDate(classOptionalDateInfo.getStartDate())
                .endDate(classOptionalDateInfo.getEndDate())
                .autoEnabled(classOptionalDateInfo.getAutoEnabled() != null ? classOptionalDateInfo.getAutoEnabled() : true)
                .build();
    }

    public static Classroom createWith(OpenClassroom openClassroom, Register register){
        return new Classroom(openClassroom, register);
    }

    /**
     * @param openClassroomValidator 수업 개설이 가능한지 제약조건 검사하는 validator
     * - 수업 개설
     */
    public void open(OpenClassroomValidator openClassroomValidator) {
        openClassroomValidator.validation(register, classDateInfo);
        state = ClassroomState.Activate;
    }

    /**
     * - 수업 활성화
     */
    public void active() {
        verifyDisable();
        state = ClassroomState.Activate;
    }

    private void verifyDisable() {
        if(state.equals(ClassroomState.Activate)){
            throw new AlreadyActiveClassException("이미 활성화된 수업입니다.");
        }
    }

    /**
     * - 수업 비활성화
     */
    public void disable() {
        verifyActivate();
        state = ClassroomState.Disable;
    }

    private void verifyActivate() {
        if(state.equals(ClassroomState.Disable)){
            throw new AlreadyDisabledClassException("이미 비활성화된 수업입니다.");
        }
    }

    /**
     * @param changeClassInfo
     * - 수업 정보 변경
     */
    public void changeClassInfo(ChangeClassInfo changeClassInfo) {
        verifyActivate();
        this.classInfo = new ClassInfo(changeClassInfo);
    }

    /**
     * @param changeClassDateInfo
     * - 수업 시간 변경
     */
    public void changeClassDateInfo(ChangeClassDateInfo changeClassDateInfo) {
        verifyActivate();
        this.classDateInfo = new ClassDateInfo(changeClassDateInfo);
    }

    /**
     * @param classOptionalDateInfo
     * - 수업 날짜 변경
     */
    public void changeClassOptionalDateInfo(ChangeClassOptionalDateInfo classOptionalDateInfo) {
        if(existChangeClassOptionalDateInfo(classOptionalDateInfo)){
            this.classOptionalDateInfo = mapFrom(classOptionalDateInfo);
        }else{
            this.classOptionalDateInfo = null;
        }
    }

    /**
     * @param requester
     * - 수강 신청
     */
    public void elrolment(ElrolmentValidator elrolmentValidator, Requester requester) {
        if(isDisable()){
            throw new AlreadyDisabledClassException("비활성화되어있는 수업에 수강신청할 수 없습니다.");
        }
        if(register.equals(Register.of(requester.get()))){
           throw new InvalidElrolmentException("자신의 수업에 수강신청을 진행할 수 없습니다.");
        }
        elrolmentValidator.validation(code, requester);
    }

    public boolean isDisable() {
        return state.equals(ClassroomState.Disable);
    }

    public ClassroomModel toModel() {
        return ClassroomModel.builder()
                .code(code.get())
                .classInfo(classInfo.toModel())
                .classDateInfo(classDateInfo.toModel())
                .classOptionalDateInfo(classOptionalDateInfo != null ? classOptionalDateInfo.toModel() : null)
                .register(register.get())
                .createDateTime(createDateTime)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classroom classroom = (Classroom) o;
        return Objects.equals(code, classroom.code) && Objects.equals(classInfo, classroom.classInfo) && Objects.equals(classDateInfo, classroom.classDateInfo) && Objects.equals(register, classroom.register) && Objects.equals(createDateTime, classroom.createDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, classInfo, classDateInfo, register, createDateTime);
    }

}
