package com.ljy.videoclass.classroom.domain;

import com.ljy.videoclass.classroom.domain.value.Color;
import com.ljy.videoclass.classroom.domain.value.Title;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeClassInfo {
    @NotBlank(message = Title.EMPTY_TITLE)
    @Pattern(regexp = Title.PATTERN_STR, message = Title.INVALID_TITLE)
    private String title;

    private Color color;
}
