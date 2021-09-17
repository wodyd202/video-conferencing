package com.ljy.videoclass.classroom.domain.value;

import com.ljy.videoclass.classroom.domain.ChangeClassInfo;
import com.ljy.videoclass.classroom.domain.read.ClassInfoModel;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Embeddable
public class ClassInfo {
    private final Title title;

    @Enumerated(EnumType.STRING)
    private final Color color;

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
