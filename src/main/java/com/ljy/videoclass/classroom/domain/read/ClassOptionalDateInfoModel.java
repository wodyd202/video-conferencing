package com.ljy.videoclass.classroom.domain.read;

import lombok.Builder;

import java.time.LocalDate;

public class ClassOptionalDateInfoModel {
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean autoEnabled;

    @Builder
    private ClassOptionalDateInfoModel(LocalDate startDate, LocalDate endDate, boolean autoEnabled) {
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

    public boolean isAutoEnabled() {
        return autoEnabled;
    }
}
