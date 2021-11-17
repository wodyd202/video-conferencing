package com.ljy.videoclass.services.elrolment.command.application.external;

import java.util.Optional;

public interface ExternalClassroomRepository {
    Optional<Classroom> getClassroom(String classroomCode);
}
