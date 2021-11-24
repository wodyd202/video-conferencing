package com.ljy.videoclass.services.conference.command.application;

import com.ljy.videoclass.services.conference.command.application.external.ExternalPanelistRepository;
import com.ljy.videoclass.services.panelist.command.application.model.Panelist;
import com.ljy.videoclass.services.panelist.domain.value.PanelistStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StubExternalPanelistRepository implements ExternalPanelistRepository {
    @Override
    public Optional<Panelist> getPanelist(String id) {
        return Optional.of(Panelist.builder()
                        .id(id)
                        .status(PanelistStatus.ACTIVE)
                .build());
    }
}
