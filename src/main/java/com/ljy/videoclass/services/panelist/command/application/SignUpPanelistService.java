package com.ljy.videoclass.services.panelist.command.application;

import com.ljy.videoclass.services.classroom.command.application.PanelistMapper;
import com.ljy.videoclass.services.classroom.command.application.model.SignUpPanalist;
import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.PanelistRepository;
import com.ljy.videoclass.services.panelist.domain.exception.AlreadyExistPanelistException;
import com.ljy.videoclass.services.panelist.domain.model.PanelistModel;
import com.ljy.videoclass.services.panelist.domain.value.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회의자 가입 서비스
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class SignUpPanelistService {
    private PanelistMapper panelistMapper;
    private PanelistRepository panelistRepository;
    private PasswordEncoder passwordEncoder;

    // 회의자 가입
    public PanelistModel signUp(SignUpPanalist signUpPanalist) {
        // 이미 해당 이메일로 가입한 회의자가 존재하는지 체크
        verifyNotExistPanelist(signUpPanalist);

        Panelist panelist = panelistMapper.mapFrom(signUpPanalist, passwordEncoder);
        panelistRepository.save(panelist);

        PanelistModel panelistModel = panelist.toModel();
        log.info("panelist save success : {}", panelistModel);
        return panelistModel;
    }

    private void verifyNotExistPanelist(SignUpPanalist signUpPanalist){
        if(panelistRepository.findById(Email.of(signUpPanalist.getEmail())).isPresent()){
            throw new AlreadyExistPanelistException();
        }
    }
}
