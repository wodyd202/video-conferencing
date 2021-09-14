package com.ljy.videoclass.user.domain.value;

import com.ljy.videoclass.user.domain.exception.InvalidPasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class Password {
    private final String pw;

    private Password(String pw, PasswordEncoder passwordEncoder){
        this.pw = passwordEncoder.encode(pw);
    }

    private Password(String pw) {
        verifyNotEmptyPw(pw);
        this.pw = pw;
    }

    public static final String EMPTY_PW = "비밀번호를 입력해주세요.";
    private void verifyNotEmptyPw(String pw) {
        if(!StringUtils.hasText(pw)){
            throw new InvalidPasswordException(EMPTY_PW);
        }
    }

    public Password encode(PasswordEncoder passwordEncoder) {
        return new Password(pw, passwordEncoder);
    }

    public static Password of(String pw) {
        return new Password(pw);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password birth1 = (Password) o;
        return Objects.equals(pw, birth1.pw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pw);
    }

    public String get() {
        return pw;
    }

}
