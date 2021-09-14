package com.ljy.videoclass.user;

import com.ljy.videoclass.user.domain.value.Password;
import com.ljy.videoclass.user.domain.User;
import com.ljy.videoclass.user.domain.value.UserId;
import com.ljy.videoclass.user.domain.value.Username;

public class UserFixture {

    public static User.UserBuilder aUser() {
        return User.builder()
                .id(UserId.of("00000000"))
                .password(Password.of("000000"))
                .username(Username.of("학생"));
    }
}
