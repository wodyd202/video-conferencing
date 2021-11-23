package com.ljy.videoclass.services.classroom.command.application;

import com.ljy.videoclass.services.classroom.command.application.model.SignUpPanalist;
import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.value.Email;
import com.ljy.videoclass.services.panelist.domain.value.Password;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PanelistMapper {

    public Panelist mapFrom(SignUpPanalist signUpPanalist, PasswordEncoder passwordEncoder) {
        Panelist panelist = Panelist.builder()
                .email(Email.of(signUpPanalist.getEmail()))
                .password(Password.of(signUpPanalist.getPassword(), passwordEncoder))
                .build();
        log.info("map from : {} to {}", signUpPanalist ,panelist);
        return panelist;
    }
}
