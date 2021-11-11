package com.ljy.videoclass.services.classroom.domain;

import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.*;
import lombok.Builder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 강의
 */
@Entity
@Table(name = "class_room",indexes = {
        @Index(columnList = "register, dayOfWeek, startDate, endDate, startHour, endHour,code, title"),
        @Index(columnList = "register, state")
})
@DynamicUpdate
public class Classroom {
    /**
     * 강의 코드
     */
    @EmbeddedId
    @AttributeOverride(name = "code", column = @Column(name = "code", length = 50))
    private ClassroomCode code;

    /**
     * 강의 정보
     */
    @Embedded
    private ClassInfo classInfo;

    /**
     * 강의 날짜
     */
    @Embedded
    private ClassDateInfo classDateInfo;

    /**
     * 강의 시작일(옵션)
     */
    @Embedded
    @AttributeOverride(name = "autoEnabled", column = @Column(name = "autoEnabledState"))
    private ClassOptionalDateInfo classOptionalDateInfo;

    /**
     * 강의 상태
     * # Activate (활성)
     * # Disable (비활성)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ClassroomState state;

    /**
     * 강의 개설자
     */
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "register", nullable = false, length = 30))
    private Register register;

    /**
     * 강의 개설일
     */
    private LocalDate createDate;

    protected Classroom(){}

    @Builder
    public Classroom(ClassInfo classInfo,
                     ClassDateInfo classDateInfo,
                     ClassOptionalDateInfo classOptionalDateInfo,
                     Register register) {
        setCode();
        this.classInfo = classInfo;
        this.classDateInfo = classDateInfo;
        setClassOptionalDateInfo(classOptionalDateInfo);
        this.state = ClassroomState.Activate;
        this.register = register;
        this.createDate = LocalDate.now();
    }

    private void setClassOptionalDateInfo(ClassOptionalDateInfo classOptionalDateInfo) {
        this.classOptionalDateInfo = classOptionalDateInfo == null ? ClassOptionalDateInfo.getInstance() : classOptionalDateInfo;
    }

    /**
     * 코드 자동생성
     */
    private void setCode() {
        this.code = ClassroomCode.create();
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
            throw new IllegalArgumentException("이미 활성화된 수업입니다.");
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
            throw new IllegalArgumentException("이미 비활성화된 수업입니다.");
        }
    }

    /**
     * @param changeClassInfo
     * - 수업 정보 변경
     */
    public void changeClassInfo(ClassInfo changeClassInfo) {
        verifyActivate();
        this.classInfo = changeClassInfo;
    }

    /**
     * @param changeClassDateInfo
     * - 수업 시간 변경
     */
    public void changeClassDateInfo(ClassDateInfo changeClassDateInfo) {
        verifyActivate();
        this.classDateInfo = changeClassDateInfo;
    }

    /**
     * @param changeClassOptionalDateInfo
     * - 수업 날짜 변경
     */
    public void changeClassOptionalDateInfo(ClassOptionalDateInfo changeClassOptionalDateInfo) {
        setClassOptionalDateInfo(changeClassOptionalDateInfo);
    }

    public ClassroomModel toModel() {
        return ClassroomModel.builder()
                .code(code.get())
                .classInfo(classInfo.toModel())
                .state(state)
                .classDateInfo(classDateInfo.toModel())
                .classOptionalDateInfo(classOptionalDateInfo != null ? classOptionalDateInfo.toModel() : null)
                .register(register.get())
                .createDate(createDate)
                .build();
    }
}
