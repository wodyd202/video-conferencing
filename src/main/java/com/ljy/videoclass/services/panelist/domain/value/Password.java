package com.ljy.videoclass.services.panelist.domain.value;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {
    private final String value;
    protected Password(){value = null;}
    private Password(String value, PasswordEncoder passwordEncoder) {
        if(value == null){
            throw new IllegalArgumentException("회의자의 비밀번호를 입력해주세요.");
        }
        this.value = passwordEncoder.encode(value);
    }

    public static Password of(String password, PasswordEncoder passwordEncoder){
        return new Password(password, passwordEncoder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Password{" +
                "value='" + value + '\'' +
                '}';
    }

    public String get() {
        return value;
    }
}
