package com.ljy.videoclass.services.panelist.command.application;

import com.ljy.videoclass.services.panelist.command.model.SignUpPanalist;
import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.value.PanelistId;
import com.ljy.videoclass.services.panelist.domain.value.Password;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PanelistMapper {

    public Panelist mapFrom(SignUpPanalist signUpPanalist, PasswordEncoder passwordEncoder) {
        Panelist panelist = Panelist.builder()
                .id(PanelistId.of(signUpPanalist.getId()))
                .password(Password.of(signUpPanalist.getPassword(), passwordEncoder))
                .build();
        log.info("map from : {} to {}", signUpPanalist ,panelist);
        return panelist;
    }
}
