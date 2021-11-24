package com.ljy.videoclass.services.conference.domain.value;

import javax.persistence.Embeddable;

@Embeddable
public class LimitCount {
    private int value;

    protected LimitCount(){}

    private LimitCount(int limitCount) {
        if(limitCount < 2 || limitCount > 10){
            throw new IllegalArgumentException("회의 제한 인원은 2인 이상 10인 이하로 입력해주세요.");
        }
        this.value = limitCount;
    }

    public static LimitCount of(int limitCount){
        return new LimitCount(limitCount);
    }

    public int get() {
        return value;
    }

    @Override
    public String toString() {
        return "LimitCount{" +
                "value=" + value +
                '}';
    }
}
