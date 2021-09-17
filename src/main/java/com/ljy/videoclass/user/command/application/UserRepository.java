package com.ljy.videoclass.user.command.application;

import com.ljy.videoclass.user.domain.User;
import com.ljy.videoclass.user.domain.value.UserId;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(UserId userId);
}
