package com.ljy.videoclass.classroom.domain.value;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Register {
    private final String id;

    protected Register(){id = null;}

    private Register(String id) {
        this.id = id;
    }

    public static Register of(String id){
        return new Register(id);
    }

    public String get() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Register register = (Register) o;
        return Objects.equals(id, register.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
