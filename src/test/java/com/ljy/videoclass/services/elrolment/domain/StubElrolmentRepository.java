package com.ljy.videoclass.services.elrolment.domain;

import com.ljy.videoclass.services.elrolment.command.application.ElrolmentRepository;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.Requester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class StubElrolmentRepository implements ElrolmentRepository {
    private HashMap<String, List<Elrolment>> repo = new HashMap<>();

    @Override
    public void save(Elrolment elrolmentUser) {
        ElrolmentModel elrolmentModel = elrolmentUser.toModel();
        List<Elrolment> orDefault = repo.getOrDefault(elrolmentModel.getClassroomCode(), new ArrayList<>());
        orDefault.add(elrolmentUser);
        repo.put(elrolmentModel.getClassroomCode(), orDefault);
    }

    @Override
    public Optional<Elrolment> findByCodeAndRequesterInfo(ClassroomCode classroomCode, Requester requester) {
        List<Elrolment> orDefault = repo.getOrDefault(classroomCode.get(), new ArrayList<>());
        for (Elrolment elrolment : orDefault) {
            ElrolmentModel elrolmentModel = elrolment.toModel();
            if(elrolmentModel.getRequester().getRequester().equals(requester.get())){
                return Optional.of(elrolment);
            }
        }
        return Optional.empty();
    }

    @Override
    public void removeByCodeAndRequesterInfo(ClassroomCode classroomCode, Requester requester) {

    }
}
