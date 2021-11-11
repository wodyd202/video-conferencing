package com.ljy.videoclass.services.classroom.command.application.model;

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
    @NotNull(message = "수업 요일을 [월,화,수,목,금,토,일] 중 하나를 입력해주세요.")
    private DayOfWeek dayOfWeek;

    @Min(value = 0, message = "수업 시작 시간을 0 이상 24 이하로 입력해주세요.")
    @Max(value = 24, message = "수업 시작 시간을 0 이상 24 이하로 입력해주세요.")
    private Integer startHour;

    @Min(value = 0, message = "수업 종료 시간을 0 이상 24 이하로 입력해주세요.")
    @Max(value = 24, message = "수업 종료 시간을 0 이상 24 이하로 입력해주세요.")
    private Integer endHour;
}
