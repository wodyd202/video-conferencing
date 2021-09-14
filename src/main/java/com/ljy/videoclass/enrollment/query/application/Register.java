package com.ljy.videoclass.enrollment.query.application;

public class Register {
    private final String id;

    private Register(String id) {
        this.id = id;
    }

    public static Register of(String id){
        return new Register(id);
    }

}
