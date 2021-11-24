package com.ljy.videoclass.services.conference.domain.value;

import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public class ConferenceTitle {
    private String value;

    protected ConferenceTitle(){}

    private static Pattern PATTEN = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z0-9\\s]{5,20}$");
    private ConferenceTitle(String value) {
        if(value == null){
            throw new IllegalArgumentException("회의 제목을 입력해주세요.");
        }
        value = value.trim();
        if(!PATTEN.matcher(value).matches()){
            throw new IllegalArgumentException("회의 제목은 [한글,숫자,영어] 조합으로 5자 이상 20자 이하로 입력해주세요.");
        }
        this.value = value;
    }

    public static ConferenceTitle of(String title){
        return new ConferenceTitle(title);
    }

    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return "ConferenceTitle{" +
                "value='" + value + '\'' +
                '}';
    }
}
