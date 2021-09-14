package com.ljy.videoclass.runner;

import com.ljy.videoclass.user.command.application.RegisterUserService;
import com.ljy.videoclass.user.command.application.model.RegisterUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {
    @Autowired private RegisterUserService registerUserService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        RegisterUser registerUser = RegisterUser.builder()
                .userId("00000000")
                .password("000000")
                .name("홍길동")
                .build();
        registerUserService.register(registerUser);
    }
}
