package com.ljy.videoclass.classroom;

import lombok.Builder;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "class_room")
public class Classroom {
    @EmbeddedId
    private final ClassroomCode code;

    @Embedded
    private ClassInfo classInfo;

    @Embedded
    private ClassDateInfo classDateInfo;

    @Embedded
    private final Register register;
    private final LocalDateTime createDateTime;

    protected Classroom(){code = null; register = null; createDateTime = null;}

    @Builder
    public Classroom(ClassInfo classInfo,
                     ClassDateInfo classDateInfo,
                     Register register) {
        this.code = ClassroomCode.create();
        this.classInfo = classInfo;
        this.classDateInfo = classDateInfo;
        this.register = register;
        this.createDateTime = LocalDateTime.now();
    }
}
