package com.ljy.videoclass.services.panelist;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PanelistMapper {

    public Panelist mapFrom(SignUpPanalist signUpPanalist) {
        Panelist panelist = Panelist.builder()
                .email(Email.of(signUpPanalist.getEmail()))
                .password(Password.of(signUpPanalist.getPassword()))
                .build();
        log.info("map from : {} to {}", signUpPanalist ,panelist);
        return panelist;
    }
}
