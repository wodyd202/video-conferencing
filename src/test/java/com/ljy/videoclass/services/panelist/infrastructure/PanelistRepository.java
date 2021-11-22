package com.ljy.videoclass.services.panelist.infrastructure;

import com.ljy.videoclass.services.panelist.Email;
import com.ljy.videoclass.services.panelist.Panelist;

import java.util.Optional;

public interface PanelistRepository {
    Optional<Panelist> findById(Email email);
    void save(Panelist panelist);
}
