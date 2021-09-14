package com.ljy.videoclass.classroom.domain;

import lombok.Builder;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Embeddable
public class ClassInfo {
    private final Title title;

    @Enumerated(EnumType.STRING)
    private final Color color;

    protected ClassInfo(){title = null; color = null;}

    @Builder
    public ClassInfo(Title title, Color color) {
        this.title = title;
        if(Objects.isNull(color)){
            this.color = Color.PRIMARY;
        }else{
            this.color = color;
        }
    }

    public Title getTitle() {
        return title;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassInfo classInfo = (ClassInfo) o;
        return Objects.equals(title, classInfo.title) && color == classInfo.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, color);
    }
}
