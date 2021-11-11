package com.ljy.videoclass.services.user.domain.value;

import com.ljy.videoclass.services.user.domain.exception.InvalidUserIdException;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserId implements Serializable {
    private final String id;

    protected UserId(){
        id = null;}

    private UserId(String id) {
        if(id.isEmpty()){
            throw new InvalidUserIdException("사용자 아이디를 입력해주세요.");
        }
        this.id = id;
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
