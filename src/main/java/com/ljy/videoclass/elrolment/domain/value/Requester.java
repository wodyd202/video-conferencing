package com.ljy.videoclass.elrolment.domain.value;

import java.util.Objects;

public class Requester {
    private final String userId;

    private Requester(String userId) {
        this.userId = userId;
    }

    public static Requester of(String userId){
        return new Requester(userId);
    }

    public String get() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requester requester = (Requester) o;
        return Objects.equals(userId, requester.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
