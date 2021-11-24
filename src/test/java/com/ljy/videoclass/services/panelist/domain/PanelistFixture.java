package com.ljy.videoclass.services.panelist.domain;

import com.ljy.videoclass.services.panelist.domain.value.PanelistId;
import com.ljy.videoclass.services.panelist.domain.value.Password;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import static org.mockito.Mockito.mock;

public class PanelistFixture {
    public static Panelist.PanelistBuilder aPanelist(){
        return Panelist.builder()
                .id(PanelistId.of("panelistId"))
                .password(Password.of("password", PasswordEncoderFactories.createDelegatingPasswordEncoder()));
    }
}
