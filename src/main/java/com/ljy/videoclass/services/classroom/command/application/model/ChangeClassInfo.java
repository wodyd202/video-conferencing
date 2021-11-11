package com.ljy.videoclass.services.classroom.command.application.model;

import com.ljy.videoclass.services.classroom.domain.value.Color;
import com.ljy.videoclass.services.classroom.domain.value.Description;
import com.ljy.videoclass.services.classroom.domain.value.Title;
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
    @NotBlank(message = "수업명을 입력해주세요.")
    @Pattern(regexp = Title.PATTERN_STR, message = "수업명은 [한글, 숫자, 영어(대,소문자)] 만 허용하며 1자 이상 20자 이하로 입력해주세요.")
    private String title;

    @Pattern(regexp = Description.PATTERN_STR, message = "수업 설명은 [한글, 숫자, 영문(대,소문자)] 만 허용하며 1자 이상 50자 이하만 허용합니다.")
    private String description;

    private Color color;
}
