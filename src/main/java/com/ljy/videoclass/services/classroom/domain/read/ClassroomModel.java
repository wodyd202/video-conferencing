package com.ljy.videoclass.services.classroom.domain.read;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ljy.videoclass.services.classroom.domain.value.ClassOptionalDateInfo;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomState;
import com.ljy.videoclass.services.classroom.domain.value.Color;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassroomModel {
    private String code;
    private ClassInfoModel classInfo;
    private ClassDateInfoModel classDateInfo;
    private ClassOptionalDateInfoModel classOptionalDateInfo;
    private ClassroomState state;
    private String register;
    private LocalDate createDate;

    public ClassroomModel(String code,
                          Color color,
                          String title,
//                          String description,
                          DayOfWeek dayOfWeek,
                          int startHour,
                          int endHour,
                          ClassOptionalDateInfo classOptionalDateInfo,
                          String register){
        this.code = code;
        this.classInfo = ClassInfoModel.builder()
                .title(title)
                .color(color)
//                .description(description)
                .build();
        this.classDateInfo = ClassDateInfoModel.builder()
                .dayOfWeek(dayOfWeek)
                .startHour(startHour)
                .endHour(endHour)
                .build();
        this.classOptionalDateInfo = classOptionalDateInfo != null ? classOptionalDateInfo.toModel() : null;
        this.register = register;
    }

    @Builder
    public ClassroomModel(String code,
                          ClassInfoModel classInfo,
                          ClassDateInfoModel classDateInfo,
                          ClassroomState state,
                          String register,
                          LocalDate createDate,
                          ClassOptionalDateInfoModel classOptionalDateInfo) {
        this.code = code;
        this.classInfo = classInfo;
        this.state = state;
        this.classDateInfo = classDateInfo;
        this.register = register;
        this.createDate = createDate;
        this.classOptionalDateInfo = classOptionalDateInfo;
    }

    @Override
    public String toString() {
        return "ClassroomModel{" +
                "code='" + code + '\'' +
                ", classInfo=" + classInfo +
                ", classDateInfo=" + classDateInfo +
                ", classOptionalDateInfo=" + classOptionalDateInfo +
                ", state=" + state +
                ", register='" + register + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
