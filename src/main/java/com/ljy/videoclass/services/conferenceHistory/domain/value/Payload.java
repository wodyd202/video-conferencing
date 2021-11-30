package com.ljy.videoclass.services.conferenceHistory.domain.value;

import javax.persistence.Embeddable;

@Embeddable
public class Payload {
    private String value;

    protected Payload(){}

    private Payload(String value) {
        this.value = value;
    }

    public static Payload of(String payload){
        return new Payload(payload);
    }

    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "value='" + value + '\'' +
                '}';
    }
}
