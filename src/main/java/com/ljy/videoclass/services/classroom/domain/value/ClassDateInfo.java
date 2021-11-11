package com.ljy.videoclass.services.classroom.domain.value;

import com.ljy.videoclass.services.classroom.domain.read.ClassDateInfoModel;
import lombok.Builder;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.DayOfWeek;
import java.util.Objects;

/**
 * 강의 날짜
 */
@Embeddable
public class ClassDateInfo {
    /**
     * 강의 요일
     */
    @Enumerated(EnumType.STRING)
    private final DayOfWeek dayOfWeek;
    /**
     * 강의 시작 시간 및 종료 시간
     */
    private final int startHour,endHour;

    protected ClassDateInfo(){dayOfWeek =null; startHour = 0; endHour = 0;}

    @Builder
    public ClassDateInfo(DayOfWeek dayOfWeek, Integer startHour, Integer endHour) {
        verifyNotEmptyDayOfWeeks(dayOfWeek);
        validation(startHour, endHour);
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    private void verifyNotEmptyDayOfWeeks(DayOfWeek dayOfWeeks) {
        if(Objects.isNull(dayOfWeeks)){
            throw new IllegalArgumentException("수업 요일을 [월,화,수,목,금,토,일] 중 하나를 입력해주세요.");
        }
    }

    private void validation(int startHour, int endHour) {
        hourValidation(startHour, "수업 시작 시간을 0 이상 24 이하로 입력해주세요.");
        hourValidation(endHour, "수업 종료 시간을 0 이상 24 이하로 입력해주세요.");
        if(startHour >= endHour){
            throw new IllegalArgumentException("수업 시작시간은 수업 종료시간보다 작아야합니다.");
        }
    }

    private void hourValidation(int hour, String exceptionMessage) {
        if(hour < 0 || hour > 24){
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassDateInfo that = (ClassDateInfo) o;
        return startHour == that.startHour && endHour == that.endHour && Objects.equals(dayOfWeek, that.dayOfWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, startHour, endHour);
    }

    @Override
    public String toString() {
        return "ClassDateInfo{" +
                "dayOfWeeks=" + dayOfWeek +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                '}';
    }

    public ClassDateInfoModel toModel() {
        return ClassDateInfoModel.builder()
                .dayOfWeek(dayOfWeek)
                .startHour(startHour)
                .endHour(endHour)
                .build();
    }
}
