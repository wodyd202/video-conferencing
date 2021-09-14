package com.ljy.videoclass.classroom.domain;

import com.ljy.videoclass.classroom.domain.value.ClassDateInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeClassDateInfo {
    @NotNull(message = ClassDateInfo.EMPTY_DAY_OF_WEEKS)
    private DayOfWeek dayOfWeek;

    @Min(value = 0, message = ClassDateInfo.INVALID_START_HOUR)
    @Max(value = 24, message = ClassDateInfo.INVALID_START_HOUR)
    private int startHour;

    @Min(value = 0, message = ClassDateInfo.INVALID_END_HOUR)
    @Max(value = 24, message = ClassDateInfo.INVALID_END_HOUR)
    private int endHour;
}
