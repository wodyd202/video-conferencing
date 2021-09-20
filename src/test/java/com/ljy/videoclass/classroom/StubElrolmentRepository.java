package com.ljy.videoclass.classroom;

import com.ljy.videoclass.elrolment.command.ElrolmentRepository;
import com.ljy.videoclass.elrolment.domain.ElrolmentUser;
import com.ljy.videoclass.classroom.domain.read.ErolmentUserModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@Primary
public class StubElrolmentRepository implements ElrolmentRepository {
    private HashMap<ClassroomCode, List<ElrolmentUser>> repo = new HashMap<>();

    @Override
    public void save(ElrolmentUser elrolmentUser) {
    }

    @Override
    public List<ErolmentUserModel> findByClassroomCode(ClassroomCode code) {
        return null;
    }
}
