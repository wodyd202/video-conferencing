package com.ljy.videoclass.services.elrolment.command.application.external;

import com.ljy.videoclass.services.elrolment.domain.value.Requester;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 수강신청시 사용되는 수업 정보
 */
@Getter
public class Classroom {
    private String classroomCode;
    private ClassOptionalDateInfo classOptionalDateInfo;
    private ClassroomState state;
    private String owner;

    @Builder
    public Classroom(String classroomCode, ClassOptionalDateInfo classOptionalDateInfo, ClassroomState state, String owner) {
        this.classroomCode = classroomCode;
        this.classOptionalDateInfo = classOptionalDateInfo;
        this.state = state;
        this.owner = owner;
    }

    /**
     * # 종강한 강의인지 체크
     */
    public boolean isFinished() {
        if(classOptionalDateInfo != null && classOptionalDateInfo.endDate != null){
            return classOptionalDateInfo.endDate.isBefore(LocalDate.now());
        }
        return false;
    }

    /**
     * # 비활성화 강의인지 체크
     */
    public boolean isDisabled() {
        return state == ClassroomState.Disable;
    }

    public boolean equalsOnwer(Requester requester) {
        return owner.equals(requester.get());
    }

    public enum ClassroomState {
        Activate, Disable
    }

    public static class ClassOptionalDateInfo {
        private LocalDate endDate;

        public ClassOptionalDateInfo(LocalDate endDate) {
            this.endDate = endDate;
        }
    }
}
