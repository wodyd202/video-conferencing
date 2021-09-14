package com.ljy.user.command.application;

import com.ljy.user.command.application.model.RegisterUser;
import com.ljy.user.command.domain.Password;
import com.ljy.user.command.domain.User;
import com.ljy.user.command.domain.UserId;
import org.springframework.stereotype.Component;

@Component
final public class UserMapper {

    public User mapFrom(RegisterUser registerUser) {
        return new User(UserId.of(registerUser.getUserId()), Password.of(registerUser.getPassword()));
    }
}
