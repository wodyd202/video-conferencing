package com.ljy.videoclass.classroom.domain.value;

import com.ljy.videoclass.classroom.domain.exception.InvalidClassOptionalDateInfoException;
import com.ljy.videoclass.classroom.domain.read.ClassOptionalDateInfoModel;
import lombok.Builder;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class ClassOptionalDateInfo {
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean autoEnabled;

    protected ClassOptionalDateInfo(){}

    @Builder
    private ClassOptionalDateInfo(LocalDate startDate, LocalDate endDate, boolean autoEnabled) {
        verifyNotEmptyAll(startDate, endDate);
        if(startDate.isAfter(endDate)){
            throw new InvalidClassOptionalDateInfoException("수업 시작일은 종료일보다 반드시 작아야합니다.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.autoEnabled = autoEnabled;
    }

    private void verifyNotEmptyAll(LocalDate startDate, LocalDate endDate) {
        if(startDate != null && endDate == null){
            throw new InvalidClassOptionalDateInfoException("수업 종료일을 입력해주세요.");
        }
        if(endDate != null && startDate == null){
            throw new InvalidClassOptionalDateInfoException("수업 시작일을 입력해주세요.");
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
