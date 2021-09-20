package com.ljy.videoclass.elrolment.domain.value;

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
}
