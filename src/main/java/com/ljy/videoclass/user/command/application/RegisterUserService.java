package com.ljy.videoclass.user.command.application;

import com.ljy.videoclass.user.domain.RegisterUser;
import com.ljy.videoclass.user.domain.RegisterUserValidator;
import com.ljy.videoclass.user.domain.User;
import com.ljy.videoclass.user.domain.read.UserModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RegisterUserValidator registerUserValidator;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserService(UserRepository userRepository,
                               UserMapper userMapper,
                               RegisterUserValidator registerUserValidator,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.registerUserValidator = registerUserValidator;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel register(RegisterUser registerUser) {
        User user = userMapper.mapFrom(registerUser);
        user.register(registerUserValidator);
        user.encodePassword(passwordEncoder);
        userRepository.save(user);
        return user.toModel();
    }
}
