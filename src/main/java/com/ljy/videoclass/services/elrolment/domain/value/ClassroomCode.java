package com.ljy.videoclass.services.elrolment.domain.value;

import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * 수업 코드
 */
@Embeddable
public class ClassroomCode {
    private String code;

    protected ClassroomCode(){}

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
