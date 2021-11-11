package com.ljy.videoclass.services.classroom.domain.value;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class ClassroomCode implements Serializable {
    private final String code;

    protected ClassroomCode(){code = null;}

    private ClassroomCode(String code) {
        this.code = code;
    }

    public static ClassroomCode create(){
        return new ClassroomCode(UUID.randomUUID().toString());
    }

    public static ClassroomCode of(String code) {
        return new ClassroomCode(code);
    }

    public String get() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassroomCode that = (ClassroomCode) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "ClassroomCode{" +
                "code='" + code + '\'' +
                '}';
    }
}
