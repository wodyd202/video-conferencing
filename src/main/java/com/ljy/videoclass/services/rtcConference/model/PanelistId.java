package com.ljy.videoclass.services.rtcConference.model;

import java.util.Objects;

public class PanelistId {
    private String value;

    private PanelistId(String value) {
        this.value = value;
    }

    public static PanelistId of(String id){
        return new PanelistId(id);
    }

    public String get(){
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PanelistId that = (PanelistId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "PanelistId{" +
                "value='" + value + '\'' +
                '}';
    }
}
