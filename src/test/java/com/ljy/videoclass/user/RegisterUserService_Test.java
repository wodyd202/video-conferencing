package com.ljy.videoclass.user;

import com.ljy.videoclass.user.command.application.RegisterUserService;
import com.ljy.videoclass.user.domain.RegisterUser;
import com.ljy.videoclass.user.domain.read.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RegisterUserService_Test {
    @Autowired
    RegisterUserService registerUserService;

    @Test
    void register(){
        RegisterUser registerUser = RegisterUser.builder()
                .userId("00000001")
                .password("000000")
                .name("홍길동")
                .build();
        UserModel user = registerUserService.register(registerUser);
        Assertions.assertNotNull(user);
    }
}
