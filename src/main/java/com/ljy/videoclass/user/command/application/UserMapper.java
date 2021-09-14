package com.ljy.videoclass.user.command.application;

import com.ljy.videoclass.user.command.application.model.RegisterUser;
import com.ljy.videoclass.user.domain.Password;
import com.ljy.videoclass.user.domain.User;
import com.ljy.videoclass.user.domain.UserId;
import com.ljy.videoclass.user.domain.Username;
import org.springframework.stereotype.Component;

@Component
final public class UserMapper {

    public User mapFrom(RegisterUser registerUser) {
        return User.builder()
                .id(UserId.of(registerUser.getUserId()))
                .password(Password.of(registerUser.getPassword()))
                .username(Username.of(registerUser.getName()))
                .build();
    }
}
