package com.ljy.videoclass.services.classroom.domain.value;

import com.ljy.videoclass.services.classroom.domain.read.ClassOptionalDateInfoModel;
import lombok.Builder;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * 강의 시작일(옵션)
 */
@Embeddable
public class ClassOptionalDateInfo {
    private static final ClassOptionalDateInfo INSTANCE = new ClassOptionalDateInfo();
    /**
     * 강의 시작일 및 종료일
     */
    private LocalDate startDate, endDate;
    /**
     * 강의 종료시 자동 비활성화
     */
    private Boolean autoEnabled;

    public static ClassOptionalDateInfo getInstance() {
        return INSTANCE;
    }

    protected ClassOptionalDateInfo(){}

    @Builder
    public ClassOptionalDateInfo(LocalDate startDate, LocalDate endDate, boolean autoEnabled) {
        verifyNotEmptyAll(startDate, endDate);
        if(startDate.isAfter(endDate)){
            throw new IllegalArgumentException("수업 시작일은 종료일보다 반드시 작아야합니다.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.autoEnabled = autoEnabled;
    }

    private void verifyNotEmptyAll(LocalDate startDate, LocalDate endDate) {
        if(startDate == null && endDate == null){
            throw new IllegalArgumentException("수업 시작일 및 종료일을 입력해주세요.");
        }
        if(startDate != null && endDate == null){
            throw new IllegalArgumentException("수업 종료일을 입력해주세요.");
        }
        if(endDate != null && startDate == null){
            throw new IllegalArgumentException("수업 시작일을 입력해주세요.");
        }
    }

    public ClassOptionalDateInfoModel toModel() {
        return ClassOptionalDateInfoModel.builder()
                .startDate(startDate)
                .endDate(endDate)
                .autoEnabled(autoEnabled)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassOptionalDateInfo that = (ClassOptionalDateInfo) o;
        return autoEnabled == that.autoEnabled && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, autoEnabled);
    }
}
