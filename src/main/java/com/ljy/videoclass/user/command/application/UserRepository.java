package com.ljy.videoclass.user.command.application;

import com.ljy.videoclass.user.domain.User;
import com.ljy.videoclass.user.domain.value.UserId;

public interface UserRepository {
    boolean existByUserId(UserId userId);
    void save(User user);
}
