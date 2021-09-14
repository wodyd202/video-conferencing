package com.ljy.videoclass.user;

import com.ljy.videoclass.user.command.application.UserMapper;
import com.ljy.videoclass.user.command.application.model.RegisterUser;
import com.ljy.videoclass.user.command.application.model.UserModel;
import com.ljy.videoclass.user.command.domain.RegisterUserValidator;
import com.ljy.videoclass.user.command.domain.User;
import com.ljy.videoclass.user.command.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RegisterUserValidator registerUserValidator;

    public RegisterUserService(UserRepository userRepository,
                               UserMapper userMapper,
                               RegisterUserValidator registerUserValidator) {
        this.userRepository = userRepository;
        this.registerUserValidator = registerUserValidator;
        this.userMapper = userMapper;
    }

    public UserModel register(RegisterUser registerUser) {
        User user = userMapper.mapFrom(registerUser);
        user.register(registerUserValidator);
        userRepository.save(user);
        return new UserModel(user);
    }
}
