package com.ljy.videoclass.classroom.command.application.model;

import com.ljy.videoclass.classroom.domain.ClassDateInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.DayOfWeek;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeClassDateInfo {
    @Size(min = 1, max = 7, message = ClassDateInfo.EMPTY_DAY_OF_WEEKS)
    @NotNull(message = ClassDateInfo.EMPTY_DAY_OF_WEEKS)
    private List<DayOfWeek> dayOfWeeks;

    @Min(value = 0, message = ClassDateInfo.INVALID_START_HOUR)
    @Max(value = 24, message = ClassDateInfo.INVALID_START_HOUR)
    private int startHour;

    @Min(value = 0, message = ClassDateInfo.INVALID_END_HOUR)
    @Max(value = 24, message = ClassDateInfo.INVALID_END_HOUR)
    private int endHour;
}
