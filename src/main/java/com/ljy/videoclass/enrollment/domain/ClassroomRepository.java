package com.ljy.videoclass.enrollment.domain;

import java.util.Optional;

public interface ClassroomRepository {
    Optional<ClassroomModel> findByCode(String code);
}
