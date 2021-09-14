package com.ljy.videoclass.classroom.domain;

import com.ljy.videoclass.classroom.domain.exception.AlreadyActiveClassException;
import com.ljy.videoclass.classroom.domain.exception.AlreadyDisabledClassException;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "class_room",indexes = {
        @Index(name = "register_index", columnList = "code, color, title, dayOfWeek, startHour, endHour, register, state, createDateTime")
})
@DynamicUpdate
public class Classroom {
    @EmbeddedId
    private final ClassroomCode code;

    @Embedded
    private ClassInfo classInfo;

    @Embedded
    private ClassDateInfo classDateInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassroomState state;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "register", nullable = false))
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
        this.classInfo = new ClassInfo(classInfo);
        this.classDateInfo = new ClassDateInfo(classDateInfo);
        this.register = register;
        createDateTime = LocalDateTime.now();
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

    public ClassroomModel toModel() {
        return ClassroomModel.builder()
                .code(code.get())
                .classInfo(classInfo.toModel())
                .classDateInfo(classDateInfo.toModel())
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

    public boolean isDisable() {
        return state.equals(ClassroomState.Disable);
    }

}
