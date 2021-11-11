package com.ljy.videoclass.services.elrolment.domain;

import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.services.elrolment.domain.value.RequesterInfo;
import lombok.Builder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 수강신청
 */
@Entity
@Table(name = "elrolment")
@DynamicUpdate
public class Elrolment {
    /**
     * 수강신청 고유키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifier;

    /**
     * 수업 코드
     */
    @Embedded
    @AttributeOverride(name = "code", column = @Column(name = "classroom_code",nullable = false, length = 50))
    private ClassroomCode classroomCode;

    /**
     * 수강 신청자 정보
     */
    @Embedded
    private RequesterInfo requesterInfo;

    /**
     * 수강 신청 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private ElrolmentState state;

    /**
     * 수강 신청일
     */
    @Column(nullable = false)
    private LocalDate elrolmentDate;

    protected Elrolment(){}

    @Builder
    public Elrolment(ClassroomCode code, RequesterInfo requesterInfo) {
        this.classroomCode = code;
        this.requesterInfo = requesterInfo;
        this.elrolmentDate = LocalDate.now();
    }

    /**
     * @param requestElrolmentValidator
     * # 수강 신청 등록
     */
    public void request(RequestElrolmentValidator requestElrolmentValidator) {
        requestElrolmentValidator.validation(classroomCode, requesterInfo);
        state = ElrolmentState.NOT;
    }

    public void allow() {
        this.state = ElrolmentState.ALLOWED;
    }

    public ElrolmentModel toModel(){
        return ElrolmentModel.builder()
                .classroomCode(classroomCode)
                .requester(requesterInfo)
                .state(state)
                .elrolmentDate(elrolmentDate)
                .build();
    }
}
