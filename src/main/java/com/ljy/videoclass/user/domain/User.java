package com.ljy.videoclass.user.domain;

import com.ljy.videoclass.user.domain.exception.InvalidNameException;
import com.ljy.videoclass.user.domain.exception.InvalidPasswordException;
import com.ljy.videoclass.user.domain.exception.InvalidUserIdException;
import com.ljy.videoclass.user.domain.infra.PasswordConverter;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Objects;

import static com.ljy.videoclass.user.domain.Password.EMPTY_PW;
import static com.ljy.videoclass.user.domain.UserId.EMPTY_STUDENT_NUMBER;
import static com.ljy.videoclass.user.domain.Username.EMPTY_NAME;

@Entity
@Table(name = "users")
public class User {

    @EmbeddedId
    private final UserId id;

    @Embedded
    private Username name;

    @Convert(converter = PasswordConverter.class)
    private Password password;

    protected User(){id = null;}

    public void register(RegisterUserValidator registerUserValidator) {
        registerUserValidator.validation(id);
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = password.encode(passwordEncoder);
    }

    @Builder
    public User(UserId id, Password password, Username username) {
        verifyNotEmptyId(id);
        verifyNotEmptyPassword(password);
        verifyNotEmptyUsername(username);
        this.id = id;
        this.password = password;
        this.name = username;
    }

    private void verifyNotEmptyId(UserId id) {
        if(Objects.isNull(id)){
            throw new InvalidUserIdException(EMPTY_STUDENT_NUMBER);
        }
    }

    private void verifyNotEmptyPassword(Password password) {
        if(Objects.isNull(password)){
            throw new InvalidPasswordException(EMPTY_PW);
        }
    }

    private void verifyNotEmptyUsername(Username username) {
        if(Objects.isNull(username)){
            throw new InvalidNameException(EMPTY_NAME);
        }
    }

    public UserId getId() {
        return id;
    }

    public Password getPassword() {
        return password;
    }

    public Username getName() {
        return name;
    }

}
