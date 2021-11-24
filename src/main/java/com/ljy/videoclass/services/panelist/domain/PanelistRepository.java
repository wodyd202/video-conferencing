package com.ljy.videoclass.services.panelist.domain;

import com.ljy.videoclass.services.panelist.domain.value.PanelistId;

import java.util.Optional;

public interface PanelistRepository {
    Optional<Panelist> findById(PanelistId email);
    void save(Panelist panelist);
}
