package com.ljy.videoclass.user.command.domain;

import com.ljy.videoclass.user.command.domain.exception.AlreadyExistUserException;
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
