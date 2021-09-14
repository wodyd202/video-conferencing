package com.ljy.videoclass.user.domain;


public interface UserRepository {
    boolean existByUserId(UserId userId);
    void save(User user);
}
