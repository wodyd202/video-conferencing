package com.ljy.videoclass.services.classroom.domain.value;

import com.ljy.videoclass.services.classroom.domain.read.ClassInfoModel;
import lombok.Builder;

import javax.persistence.*;
import java.util.Objects;

/**
 * 강의 정보
 */
@Embeddable
public class ClassInfo {

    /**
     * 타이틀
     */
    @AttributeOverride(name = "title", column = @Column(name = "title", length = 40, nullable = false))
    private Title title;

    /**
     * 강의 대표 색상
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Color color;

    /**
     * 강의 목표
     */
    @AttributeOverride(name = "description", column = @Column(name = "description", length = 50))
    private Description description;

    protected ClassInfo(){}

    @Builder
    public ClassInfo(Title title, Color color, Description description) {
        setTitle(title);
        setColor(color);
        this.description = description;
    }

    private void setTitle(Title title) {
        if(title == null){
            throw new IllegalArgumentException("수업명을 입력해주세요.");
        }
        this.title = title;
    }

    private void setColor(Color color) {
        if(color == null){
            this.color = Color.primary;
            return;
        }
        this.color = color;
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

    public ClassInfoModel toModel() {
        return ClassInfoModel.builder()
                .title(title.get())
                .color(color)
                .description(description != null ? description.get() : null)
                .build();
    }
}
