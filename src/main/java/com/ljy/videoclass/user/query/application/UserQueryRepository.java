package com.ljy.videoclass.user.query.application;

import com.ljy.videoclass.user.domain.read.UserModel;

import java.util.Optional;

public interface UserQueryRepository {
    Optional<UserModel> findLoginInfoByUserId(String userId);
}
