package com.ljy.videoclass.elrolment.domain.value;

import java.util.Objects;

public class ClassroomCode {
    private final String code;

    private ClassroomCode(String code) {
        this.code = code;
    }

    public static ClassroomCode of(String code){
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
}
