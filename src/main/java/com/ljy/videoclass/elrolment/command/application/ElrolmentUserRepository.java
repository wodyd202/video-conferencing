package com.ljy.videoclass.elrolment.command.application;

import com.ljy.videoclass.elrolment.command.application.model.UserModel;

import java.util.Optional;

public interface ElrolmentUserRepository {
    Optional<UserModel> findById(String id);
}
