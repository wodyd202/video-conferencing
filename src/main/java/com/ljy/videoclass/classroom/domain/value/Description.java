package com.ljy.videoclass.classroom.domain.value;

import com.ljy.videoclass.classroom.domain.exception.InvalidDescriptionException;

import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Description {
    private final String description;

    protected Description(){
        description = null;}

    private Description(String description) {
        verifyNotEmptyContent(description);
        validation(description);
        this.description = description;
    }

    private void verifyNotEmptyContent(String description) {
        if(description.isEmpty()){
            throw new InvalidDescriptionException("수업 설명을 입력해주세요.");
        }
    }

    public static final String PATTERN_STR = "^[ㄱ-ㅎ가-힣\\da-zA-Z\\s]{1,50}$";
    private static final Pattern PATTERN = Pattern.compile(PATTERN_STR);
    public static final String INVALID_DESCRIPTION = "수업 설명은 [한글, 숫자, 영문(대,소문자)] 만 허용하며 1자 이상 50자 이하만 허용합니다.";
    private void validation(String description){
        if(!PATTERN.matcher(description).matches()){
            throw new InvalidDescriptionException(INVALID_DESCRIPTION);
        }
    }

    public static Description of(String description){
        return new Description(description);
    }

    public String get() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
