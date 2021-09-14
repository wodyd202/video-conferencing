package com.ljy.videoclass.user.domain;

import com.ljy.videoclass.user.command.application.UserRepository;
import com.ljy.videoclass.user.domain.exception.AlreadyExistUserException;
import com.ljy.videoclass.user.domain.value.UserId;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserValidator {
    private final UserRepository userRepository;

    public RegisterUserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validation(UserId userId) {
        verifyNotExistUser(userId);
    }

    private void verifyNotExistUser(UserId userId) {
        if(userRepository.existByUserId(userId)){
            throw new AlreadyExistUserException();
        }
    }
}
