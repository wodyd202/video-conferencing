package com.ljy.videoclass.services.classroom.domain.read;

import lombok.Builder;

import java.time.LocalDate;

public class ClassOptionalDateInfoModel {
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean autoEnabled;

    @Builder
    private ClassOptionalDateInfoModel(LocalDate startDate, LocalDate endDate, Boolean autoEnabled) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.autoEnabled = autoEnabled;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Boolean isAutoEnabled() {
        return autoEnabled;
    }

    @Override
    public String toString() {
        return "ClassOptionalDateInfoModel{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", autoEnabled=" + autoEnabled +
                '}';
    }
}
