package com.ljy.videoclass.services.panelist.domain.value;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class PanelistId implements Serializable {
    private final String value;

    protected PanelistId(){value = null;}
    private static Pattern PATTEN = Pattern.compile("^[a-zA-Z0-9]{5,15}$");
    private PanelistId(String value) {
        if(value == null){
            throw new IllegalArgumentException("회의자의 아이디를 입력해주세요.");
        }
        if(!PATTEN.matcher(value).matches()){
            throw new IllegalArgumentException("회의자의 아이디는 [영어, 숫자 조합으로 5자 이상 15자 이하로 입력해주세요].");
        }
        this.value = value;
    }

    public static PanelistId of(String email){
        return new PanelistId(email);
    }

    public String get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PanelistId email = (PanelistId) o;
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
