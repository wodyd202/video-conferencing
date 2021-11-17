package com.ljy.videoclass.services.elrolment.command.application.external;

import java.util.Optional;

public interface ExternalUserRepository {
    Optional<UserInfo> getUser(String userId);
}
