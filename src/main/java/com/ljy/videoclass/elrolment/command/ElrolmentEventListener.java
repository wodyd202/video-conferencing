package com.ljy.videoclass.elrolment.command;

import com.ljy.videoclass.classroom.command.application.event.AllowedElorlmentEvent;
import com.ljy.videoclass.classroom.command.application.event.ElrolmentedEvent;
import com.ljy.videoclass.elrolment.domain.ElrolmentUser;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.Requester;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ElrolmentEventListener {
    private final ElrolmentRepository elrolmentRepository;

    public ElrolmentEventListener(ElrolmentRepository elrolmentRepository) {
        this.elrolmentRepository = elrolmentRepository;
    }

    @EventListener
    void elrolment(ElrolmentedEvent event){
        ElrolmentUser elrolment = ElrolmentUser.elrolment(ClassroomCode.of(event.getCode()), Requester.of(event.getRequester()));
        elrolmentRepository.save(elrolment);
    }

    @EventListener
    void allow(AllowedElorlmentEvent event){
        ElrolmentUser elrolmentUser = elrolmentRepository.findByCodeAndRequester(ClassroomCode.of(event.getClassroomCode()), Requester.of(event.getRequester())).get();
        elrolmentUser.allow();
    }
}
