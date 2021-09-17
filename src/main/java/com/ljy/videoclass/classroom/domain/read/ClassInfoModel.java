package com.ljy.videoclass.classroom.domain.read;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ljy.videoclass.classroom.domain.value.Color;
import com.ljy.videoclass.classroom.domain.value.Description;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassInfoModel {
    private String title;
    private Color color;
    private String description;

    @Builder
    public ClassInfoModel(String title, Color color, String description) {
        this.title = title;
        this.color = color;
        this.description = description;
    }

    public Color getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
