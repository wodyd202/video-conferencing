package com.ljy.videoclass.elrolment.command.application;

import com.ljy.videoclass.classroom.command.application.event.AllowedElorlmentEvent;
import com.ljy.videoclass.classroom.command.application.event.ElrolmentedEvent;
import com.ljy.videoclass.elrolment.command.application.model.UserModel;
import com.ljy.videoclass.elrolment.domain.ElrolmentUser;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.domain.value.Requester;
import com.ljy.videoclass.elrolment.domain.value.RequesterInfo;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ElrolmentEventListener {
    private final ElrolmentRepository elrolmentRepository;
    private final ElrolmentUserRepository userRepository;

    public ElrolmentEventListener(ElrolmentRepository elrolmentRepository, ElrolmentUserRepository userRepository) {
        this.elrolmentRepository = elrolmentRepository;
        this.userRepository = userRepository;
    }

    @EventListener
    void elrolment(ElrolmentedEvent event){
        UserModel userModel = userRepository.findById(event.getRequester()).get();
        ElrolmentUser elrolment = ElrolmentUser.elrolment(ClassroomCode.of(event.getCode()), RequesterInfo.builder()
                        .requester(userModel.getId())
                        .requesterEmail(userModel.getEmail())
                        .requesterImage(userModel.getImage())
                        .requesterName(userModel.getUsername())
                .build());
        elrolmentRepository.save(elrolment);
    }

    @EventListener
    void allow(AllowedElorlmentEvent event){
        ElrolmentUser elrolmentUser = elrolmentRepository.findByCodeAndRequester(ClassroomCode.of(event.getClassroomCode()), Requester.of(event.getRequester())).get();
        elrolmentUser.allow();
    }
}
