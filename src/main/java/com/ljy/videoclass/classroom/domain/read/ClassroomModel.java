package com.ljy.videoclass.classroom.domain.read;

import com.ljy.videoclass.classroom.domain.Classroom;
import com.ljy.videoclass.classroom.domain.value.Color;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class ClassroomModel {
    private String code;
    private ClassInfoModel classInfo;
    private ClassDateInfoModel classDateInfo;
    private String register;
    private LocalDateTime createDateTime;

    public ClassroomModel(String code,
                          Color color,
                          String title,
                          DayOfWeek dayOfWeek,
                          int startHour,
                          int endHour,
                          String register){
        this.code = code;
        this.classInfo = ClassInfoModel.builder()
                .title(title)
                .color(color)
                .build();
        this.classDateInfo = ClassDateInfoModel.builder()
                .dayOfWeek(dayOfWeek)
                .startHour(startHour)
                .endHour(endHour)
                .build();
        this.register = register;
    }

    @Builder
    public ClassroomModel(String code, ClassInfoModel classInfo, ClassDateInfoModel classDateInfo, String register, LocalDateTime createDateTime) {
        this.code = code;
        this.classInfo = classInfo;
        this.classDateInfo = classDateInfo;
        this.register = register;
        this.createDateTime = createDateTime;
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
}
