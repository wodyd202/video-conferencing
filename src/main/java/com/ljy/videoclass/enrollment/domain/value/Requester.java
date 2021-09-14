package com.ljy.videoclass.enrollment.domain.value;

import java.util.Objects;

public class Requester {
    private final String id;

    private Requester(String id) {
        this.id = id;
    }

    public static Requester of(String id){
        return new Requester(id);
    }

    public String get() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requester requester = (Requester) o;
        return Objects.equals(id, requester.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
