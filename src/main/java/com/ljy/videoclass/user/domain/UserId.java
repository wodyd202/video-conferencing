package com.ljy.videoclass.user.command.domain;

import com.ljy.videoclass.user.command.domain.exception.InvalidUserIdException;
import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class UserId implements Serializable {
    private final String id;

    protected UserId(){
        id = null;}

    private UserId(String id) {
        verifyNotEmptyStudentNumber(id);
        validation(id);
        this.id = id;
    }

    private static final String EMPTY_STUDENT_NUMBER = "학번을 입력해주세요.";
    private void verifyNotEmptyStudentNumber(String id) {
        if(!StringUtils.hasText(id)){
            throw new InvalidUserIdException(EMPTY_STUDENT_NUMBER);
        }
    }

    private static final Pattern PATTERN = Pattern.compile("^[\\d]{8}$");
    private static final String INVALID_STUDENT_NUMBER = "학번은 숫자 8자만 허용합니다.";
    private void validation(String id) {
        if(!PATTERN.matcher(id).matches()){
            throw new InvalidUserIdException(INVALID_STUDENT_NUMBER);
        }
    }

    public static UserId of(String id) {
        return new UserId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId that = (UserId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String get() {
        return id;
    }
}
