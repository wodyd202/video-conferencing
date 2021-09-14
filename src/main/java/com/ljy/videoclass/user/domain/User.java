package com.ljy.videoclass.user.command.domain;

import com.ljy.videoclass.user.command.domain.infra.PasswordConverter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @EmbeddedId
    private final UserId id;

    @Convert(converter = PasswordConverter.class)
    private Password password;

    protected User(){id = null; password = null;}

    public User(UserId id, Password password) {
        this.id = id;
        this.password = password;
    }

    public UserId getId() {
        return id;
    }

    public Password getPassword() {
        return password;
    }

    public void register(RegisterUserValidator registerUserValidator) {
        registerUserValidator.validation(id);
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = password.encode(passwordEncoder);
    }
}
