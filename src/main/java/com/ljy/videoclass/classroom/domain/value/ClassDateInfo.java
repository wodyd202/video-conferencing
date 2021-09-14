package com.ljy.videoclass.classroom.domain.value;

import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.exception.ClassTimeOverlapException;
import com.ljy.videoclass.classroom.domain.exception.InvalidClassDateInfoException;
import com.ljy.videoclass.classroom.domain.read.ClassDateInfoModel;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.DayOfWeek;
import java.util.Objects;

@Embeddable
public class ClassDateInfo {
    @Enumerated(EnumType.STRING)
    private final DayOfWeek dayOfWeek;
    private final int startHour,endHour;

    protected ClassDateInfo(){dayOfWeek =null; startHour = 0; endHour = 0;}

    public ClassDateInfo(ChangeClassDateInfo classDateInfo) {
        verifyNotEmptyDayOfWeeks(classDateInfo.getDayOfWeek());
        validation(classDateInfo.getStartHour(), classDateInfo.getEndHour());
        this.dayOfWeek = classDateInfo.getDayOfWeek();
        this.startHour = classDateInfo.getStartHour();
        this.endHour = classDateInfo.getEndHour();
    }

    public static final String EMPTY_DAY_OF_WEEKS = "수업 요일을 [월,화,수,목,금,토,일] 중 하나를 입력해주세요.";
    private void verifyNotEmptyDayOfWeeks(DayOfWeek dayOfWeeks) {
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
