package com.ljy.videoclass.classroom.domain;

import com.ljy.videoclass.classroom.domain.exception.InvalidClassDateInfoException;
import com.ljy.videoclass.classroom.domain.infra.DayOfWeeksConverter;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.time.DayOfWeek;
import java.util.Objects;
import java.util.Set;

@Embeddable
public class ClassDateInfo {
    @Convert(converter = DayOfWeeksConverter.class)
    private final Set<DayOfWeek> dayOfWeeks;
    private final int startHour,endHour;

    protected ClassDateInfo(){dayOfWeeks =null; startHour = 0; endHour = 0;}

    private ClassDateInfo(Set<DayOfWeek> dayOfWeeks, int startHour, int endHour) {
        verifyNotEmptyDayOfWeeks(dayOfWeeks);
        validation(startHour, endHour);
        this.dayOfWeeks = dayOfWeeks;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public static final String EMPTY_DAY_OF_WEEKS = "수업 요일을 [월,화,수,목,금,토,일] 중 하나 이상 입력해주세요.";
    private void verifyNotEmptyDayOfWeeks(Set<DayOfWeek> dayOfWeeks) {
        if(Objects.isNull(dayOfWeeks)){
            throw new InvalidClassDateInfoException(EMPTY_DAY_OF_WEEKS);
        }
    }

    public static final String INVALID_START_HOUR = "수업 시작 시간을 0 이상 24 이하로 입력해주세요.";
    public static final String INVALID_END_HOUR = "수업 종료 시간을 0 이상 24 이하로 입력해주세요.";
    public static final String INVALID_START_END_HOUR = "수업 시작시간은 수업 종료시간보다 작아야합니다.";
    private void validation(int startHour, int endHour) {
        hourValidation(startHour, INVALID_START_HOUR);
        hourValidation(endHour, INVALID_END_HOUR);
        if(startHour >= endHour){
            throw new InvalidClassDateInfoException(INVALID_START_END_HOUR);
        }
    }

    private void hourValidation(int hour, String exceptionMessage) {
        if(hour < 0 || hour > 24){
            throw new InvalidClassDateInfoException(exceptionMessage);
        }
    }

    public static ClassDateInfo withDayOfWeeksAndHours(Set<DayOfWeek> dayOfWeeks, int startHour, int endHour) {
        return new ClassDateInfo(dayOfWeeks, startHour, endHour);
    }

    public int getEndHour() {
        return endHour;
    }

    public int getStartHour() {
        return startHour;
    }

    public Set<DayOfWeek> getDayOfWeeks() {
        return dayOfWeeks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassDateInfo that = (ClassDateInfo) o;
        return startHour == that.startHour && endHour == that.endHour && Objects.equals(dayOfWeeks, that.dayOfWeeks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeeks, startHour, endHour);
    }

    @Override
    public String toString() {
        return "ClassDateInfo{" +
                "dayOfWeeks=" + dayOfWeeks +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                '}';
    }
}
