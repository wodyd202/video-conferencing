package com.ljy.videoclass.services.user.command.application;

import com.ljy.videoclass.services.user.domain.User;
import com.ljy.videoclass.services.user.domain.value.UserId;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(UserId userId);
}
