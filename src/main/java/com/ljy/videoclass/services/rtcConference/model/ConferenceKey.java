package com.ljy.videoclass.services.rtcConference.model;

import java.util.Objects;

public class ConferenceKey {
    private String value;

    private ConferenceKey(String value) {
        this.value = value;
    }

    public static ConferenceKey of(String key){
        return new ConferenceKey(key);
    }

    public String get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConferenceKey that = (ConferenceKey) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
