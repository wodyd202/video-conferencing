package com.ljy.videoclass.user.domain.value;

import com.ljy.videoclass.user.domain.exception.InvalidNameException;
import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Username {
    private final String name;
    protected Username(){name=null;}

    private Username(String name) {
        verifyNotEmptyName(name);
        validation(name);
        this.name = name;
    }

    public static final String EMPTY_NAME = "이름을 입력해주세요.";
    private void verifyNotEmptyName(String name) {
        if(!StringUtils.hasText(name)){
            throw new InvalidNameException(EMPTY_NAME);
        }
    }

    private static final Pattern PATTERN = Pattern.compile("^[가-힣]{1,10}$");
    private static final String INVALID_NAME = "이름은 한글 1자 이상 10자 이하로 입력해주세요.";
    private void validation(String name) {
        if(!PATTERN.matcher(name).matches()){
            throw new InvalidNameException(INVALID_NAME);
        }
    }

    public static Username of(String name){
        return new Username(name);
    }

    public String get() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return Objects.equals(name, username.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
