package com.ljy.videoclass.user.command.application;

import com.ljy.videoclass.user.domain.RegisterUser;
import com.ljy.videoclass.user.domain.value.Password;
import com.ljy.videoclass.user.domain.User;
import com.ljy.videoclass.user.domain.value.UserId;
import com.ljy.videoclass.user.domain.value.Username;
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
