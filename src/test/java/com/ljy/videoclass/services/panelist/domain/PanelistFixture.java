package com.ljy.videoclass.services.panelist.domain;

import com.ljy.videoclass.services.panelist.domain.value.Email;
import com.ljy.videoclass.services.panelist.domain.value.Password;

public class PanelistFixture {
    public static Panelist.PanelistBuilder aPanelist(){
        return Panelist.builder()
                .email(Email.of("email@google.com"))
                .password(Password.of("password"));
    }
}
