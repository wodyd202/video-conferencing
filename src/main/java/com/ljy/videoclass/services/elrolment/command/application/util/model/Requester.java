package com.ljy.videoclass.services.elrolment.command.application.util.model;

public class Requester {
    private String id;

    private Requester(String id) {
        this.id = id;
    }

    public static Requester of(String id){return new Requester(id);}

    public String get() {
        return id;
    }
}
