package com.ljy.videoclass.classroom.domain.read;

import com.ljy.videoclass.classroom.domain.value.Color;
import lombok.Builder;

public class ClassInfoModel {
    private String title;
    private Color color;

    @Builder
    public ClassInfoModel(String title, Color color) {
        this.title = title;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }
}
