package com.ljy.videoclass.services.elrolment.application;

import com.ljy.videoclass.services.elrolment.command.application.external.ExternalUserRepository;
import com.ljy.videoclass.services.elrolment.command.application.external.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class StubExternalUserRepository implements ExternalUserRepository {
    private HashMap<String, UserInfo> repo = new HashMap<>();

    @Override
    public Optional<UserInfo> getUser(String userId) {
        return Optional.ofNullable(repo.get(userId));
    }

    public void save(String userId){
        repo.put(userId, UserInfo.builder()
                        .requester(userId)
                        .requesterEmail(userId)
                        .requesterImage(userId)
                        .requesterName(userId)
                .build());
    }
}
