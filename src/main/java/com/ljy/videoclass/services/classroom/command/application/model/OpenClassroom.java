package com.ljy.videoclass.services.classroom.command.application.model;

import lombok.*;

import javax.validation.Valid;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OpenClassroom {

    @Valid
    private ChangeClassInfo classInfo;

    @Valid
    private ChangeClassDateInfo classDateInfo;

    private ChangeClassOptionalDateInfo classOptionalDateInfo;
}