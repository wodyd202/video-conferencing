package com.ljy.videoclass.services.conferenceHistory.domain.value;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ConferenceCode implements Serializable {
    private String value;

    private ConferenceCode(String code) {
        if(code == null){
            throw new IllegalArgumentException("회의실 코드를 입력해주세요.");
        }
        this.value = code;
    }

    public static ConferenceCode of(String code){
        return new ConferenceCode(code);
    }

    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return "ConferenceCode{" +
                "value='" + value + '\'' +
                '}';
    }
}
