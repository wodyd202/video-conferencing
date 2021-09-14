package com.ljy.videoclass.user.domain;

import com.ljy.videoclass.user.domain.infra.PasswordConverter;
import com.ljy.videoclass.user.domain.read.UserModel;
import com.ljy.videoclass.user.domain.value.*;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

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
        this.id = id;
        this.password = password;
        this.name = username;
    }

    public UserModel toModel(){
        return UserModel.builder()
                .userId(id)
                .username(name)
                .password(password)
                .build();
    }

}
