package com.ljy.videoclass.services.conferenceHistory.domain.value;

import javax.persistence.Embeddable;

@Embeddable
public class Creator {
    private String value;

    protected Creator(){}

    private Creator(String id) {
        if(id == null){
            throw new IllegalArgumentException("회의 개최자를 입력해주세요.");
        }
        this.value = id;
    }

    public static Creator of(String id){
        return new Creator(id);
    }

    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return "Creator{" +
                "value='" + value + '\'' +
                '}';
    }
}
