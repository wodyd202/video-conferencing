package com.ljy.videoclass.services.user.domain.value;

import com.ljy.videoclass.services.user.domain.exception.InvalidNameException;

import java.util.Objects;

public class Username {
    private final String name;
    protected Username(){name=null;}

    private Username(String name) {
        if(name.isEmpty()){
            throw new InvalidNameException("사용자 이름을 입력해주세요.");
        }
        this.name = name;
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
