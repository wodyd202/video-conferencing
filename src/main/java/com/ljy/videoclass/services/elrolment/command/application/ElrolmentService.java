package com.ljy.videoclass.services.elrolment.command.application;

import com.ljy.videoclass.services.elrolment.command.application.exception.ElrolmentNotFoundException;
import com.ljy.videoclass.services.elrolment.command.application.exception.UserNotFoundException;
import com.ljy.videoclass.services.elrolment.command.application.external.ExternalUserRepository;
import com.ljy.videoclass.services.elrolment.command.application.external.UserInfo;
import com.ljy.videoclass.services.elrolment.domain.Elrolment;
import com.ljy.videoclass.services.elrolment.domain.RequestElrolmentValidator;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.Requester;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ElrolmentService {
    private ElrolmentMapper elrolmentMapper;
    private RequestElrolmentValidator requestElrolmentValidator;
    private ElrolmentRepository elrolmentRepository;

    private ExternalUserRepository userRepository;

    /**
     * @param classroomCode
     * @param requester
     * # 수강신청
     */
    public ElrolmentModel request(ClassroomCode classroomCode, Requester requester) {
        // get user
        UserInfo userInfo = userRepository.getUser(requester.get()).orElseThrow(UserNotFoundException::new);
        Elrolment elrolment = elrolmentMapper.mapFrom(classroomCode, userInfo);
        elrolment.request(requestElrolmentValidator);

        // save
        elrolmentRepository.save(elrolment);
        ElrolmentModel elrolmentModel = elrolment.toModel();

        log.info("save elrolment into database : {}", elrolmentModel);
        return elrolmentModel;
    }

    /**
     * @param classroomCode
     * @param requester
     * # 수강신청 철회
     */
    public void cencel(ClassroomCode classroomCode, Requester requester) {
        elrolmentRepository.findByCodeAndRequesterInfo(classroomCode, requester).orElseThrow(ElrolmentNotFoundException::new);
        elrolmentRepository.removeByCodeAndRequesterInfo(classroomCode, requester);
    }
}
