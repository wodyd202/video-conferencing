package com.ljy.videoclass.classroom.domain.value;

import com.ljy.videoclass.classroom.domain.ChangeClassInfo;
import com.ljy.videoclass.classroom.domain.read.ClassInfoModel;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
public class ClassInfo {

    @AttributeOverride(name = "title", column = @Column(name = "title", length = 40, nullable = false))
    private final Title title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private final Color color;

    @AttributeOverride(name = "description", column = @Column(name = "description", length = 50))
    private Description description;

    protected ClassInfo(){title = null; color = null;}

    public ClassInfo(ChangeClassInfo classInfo) {
        this.title = Title.of(classInfo.getTitle());
        setDescription(classInfo.getDescription());

        if(Objects.isNull(classInfo.getColor())){
            this.color = Color.primary;
        }else{
            this.color = classInfo.getColor();
        }
    }

    private void setDescription(String description) {
        if(!Objects.isNull(description)){
            this.description = Description.of(description);
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

    public ClassInfoModel toModel() {
        return ClassInfoModel.builder()
                .title(title.get())
                .color(color)
                .description(description != null ? description.get() : null)
                .build();
    }
}
