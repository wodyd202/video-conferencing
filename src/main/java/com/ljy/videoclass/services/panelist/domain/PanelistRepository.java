package com.ljy.videoclass.services.panelist.domain;

import com.ljy.videoclass.services.panelist.domain.value.Email;
import com.ljy.videoclass.services.panelist.domain.Panelist;

import java.util.Optional;

public interface PanelistRepository {
    Optional<Panelist> findById(Email email);
    void save(Panelist panelist);
}
