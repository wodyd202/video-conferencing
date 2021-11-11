package com.ljy.videoclass.services.classroom.domain.read;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.DayOfWeek;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassDateInfoModel {
    private DayOfWeek dayOfWeek;
    private int startHour, endHour;

    @Builder
    public ClassDateInfoModel(DayOfWeek dayOfWeek, int startHour, int endHour) {
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    @Override
    public String toString() {
        return "ClassDateInfoModel{" +
                "dayOfWeek=" + dayOfWeek +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                '}';
    }
}
