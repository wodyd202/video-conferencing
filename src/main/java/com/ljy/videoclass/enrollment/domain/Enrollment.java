package com.ljy.videoclass.enrollment.domain;

import com.ljy.videoclass.enrollment.domain.infra.ClassroomCodeConverter;
import com.ljy.videoclass.enrollment.domain.infra.RequesterConverter;
import com.ljy.videoclass.enrollment.domain.read.EnrollmentModel;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.EnrollmentCode;
import com.ljy.videoclass.enrollment.domain.value.EnrollmentState;
import com.ljy.videoclass.enrollment.domain.value.Requester;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "enrollment")
public class Enrollment {
    @EmbeddedId
    private final EnrollmentCode code;

    @Convert(converter = ClassroomCodeConverter.class)
    @Column(nullable = false)
    private final ClassroomCode classroomCode;

    @Convert(converter = RequesterConverter.class)
    @Column(nullable = false)
    private final Requester requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentState state;

    protected Enrollment(){
        code = null; classroomCode = null; requester = null;
    }

    private Enrollment(ClassroomCode classroomCode, Requester requester) {
        this.classroomCode = classroomCode;
        this.requester = requester;
        code = EnrollmentCode.create();
    }

    public static Enrollment createWith(ClassroomCode code, Requester requester) {
        return new Enrollment(code, requester);
    }

    /**
     * @param enrollmentRequsetValidator
     * - 수강 신청
     */
    public void requestEnrollment(EnrollmentRequsetValidator enrollmentRequsetValidator) {
        enrollmentRequsetValidator.validation(requester, classroomCode);
        state = EnrollmentState.Disapproval;
    }

    public EnrollmentModel toModel(){
        return EnrollmentModel.builder()
                .classroomCode(classroomCode)
                .requester(requester)
                .state(state)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(classroomCode, that.classroomCode) && Objects.equals(requester, that.requester) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(classroomCode, requester, state);
    }
}
