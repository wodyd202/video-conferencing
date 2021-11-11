package com.ljy.videoclass.services.elrolment.command.application;

import com.ljy.videoclass.services.elrolment.command.application.model.UserModel;

import java.util.Optional;

public interface ElrolmentUserRepository {
    Optional<UserModel> findById(String id);
}
