package com.ljy.videoclass;

import com.ljy.videoclass.services.panelist.command.application.SignUpPanelistService;
import com.ljy.videoclass.services.panelist.command.model.SignUpPanalist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;

import java.awt.print.Book;

@Component
@Profile("!test")
public class AppRunner implements ApplicationRunner {
    @Autowired
    private SignUpPanelistService signUpPanelistService;

    @Override
    public void run(ApplicationArguments args) {
        SignUpPanalist signUpPanalist = SignUpPanalist.builder()
                .id("dlwodyd202")
                .password("password")
                .build();
        signUpPanelistService.signUp(signUpPanalist);

        signUpPanalist = SignUpPanalist.builder()
                .id("dlwodyd2002")
                .password("password")
                .build();
        signUpPanelistService.signUp(signUpPanalist);
    }
}
