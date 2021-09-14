package com.ljy.videoclass.classroom.command.application.model;

import com.ljy.videoclass.classroom.domain.Color;
import com.ljy.videoclass.classroom.domain.Title;
import lombok.*;

import javax.annotation.RegEx;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OpenClassroom {

    @NotBlank(message = Title.EMPTY_TITLE)
    @Pattern(regexp = Title.PATTERN_STR, message = Title.INVALID_TITLE)
    private String title;

    @Valid
    private ChangeClassDateInfo classDateInfo;

    private Color color;
}
