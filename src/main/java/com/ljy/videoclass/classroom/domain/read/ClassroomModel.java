package com.ljy.videoclass.classroom.domain.read;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ljy.videoclass.classroom.domain.value.ClassOptionalDateInfo;
import com.ljy.videoclass.classroom.domain.value.Color;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassroomModel {
    private String code;
    private ClassInfoModel classInfo;
    private ClassDateInfoModel classDateInfo;
    private ClassOptionalDateInfoModel classOptionalDateInfo;
    private String register;
    private LocalDateTime createDateTime;

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
                          String register,
                          LocalDateTime createDateTime,
                          ClassOptionalDateInfoModel classOptionalDateInfo) {
        this.code = code;
        this.classInfo = classInfo;
        this.classDateInfo = classDateInfo;
        this.register = register;
        this.createDateTime = createDateTime;
        this.classOptionalDateInfo = classOptionalDateInfo;
    }

    public String getCode() {
        return code;
    }

    public ClassInfoModel getClassInfo() {
        return classInfo;
    }

    public ClassDateInfoModel getClassDateInfo() {
        return classDateInfo;
    }

    public String getRegister() {
        return register;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public ClassOptionalDateInfoModel getClassOptionalDateInfo() {
        return classOptionalDateInfo;
    }
}
