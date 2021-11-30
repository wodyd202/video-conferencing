package com.ljy.videoclass.services.panelist.domain;

import com.ljy.videoclass.services.panelist.domain.value.PanelistId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PanelistRepository extends JpaRepository<Panelist, PanelistId> {
}
