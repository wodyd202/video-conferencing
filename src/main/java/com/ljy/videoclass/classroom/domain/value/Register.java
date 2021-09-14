package com.ljy.videoclass.classroom.domain;

import javax.persistence.Embeddable;

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
}
