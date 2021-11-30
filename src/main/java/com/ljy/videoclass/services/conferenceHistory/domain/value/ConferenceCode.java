package com.ljy.videoclass.services.conferenceHistory.domain.value;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ConferenceCode {
    private String value;

    protected ConferenceCode(){}

    private ConferenceCode(String value) {
        this.value = value;
    }

    public static ConferenceCode of(String code){
        return new ConferenceCode(code);
    }

    public String get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConferenceCode that = (ConferenceCode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ConferenceCode{" +
                "value='" + value + '\'' +
                '}';
    }
}
