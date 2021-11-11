package com.ljy.videoclass.services.classroom.command.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeClassOptionalDateInfo {
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean autoEnabled;
}
