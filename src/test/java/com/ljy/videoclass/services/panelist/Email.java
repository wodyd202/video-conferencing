package com.ljy.videoclass.services.panelist;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

public class Email implements Serializable {
    private final String value;

    protected Email(){value = null;}
    private static Pattern PATTEN = Pattern.compile("[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
    private Email(String value) {
        if(value == null){
            throw new IllegalArgumentException("회의자 이메일을 입력해주세요.");
        }
        if(!PATTEN.matcher(value).matches()){
            throw new IllegalArgumentException("회의자 이메일이 올바르지 않습니다. 이메일을 다시 확인해주세요.");
        }
        this.value = value;
    }

    public static Email of(String email){
        return new Email(email);
    }

    public String get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Email{" +
                "value='" + value + '\'' +
                '}';
    }
}
