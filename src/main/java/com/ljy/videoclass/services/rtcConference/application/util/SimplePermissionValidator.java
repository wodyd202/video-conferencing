package com.ljy.videoclass.services.rtcConference.application.util;

import com.ljy.videoclass.services.rtcConference.model.PermissionValidator;
import com.ljy.videoclass.services.rtcConference.application.external.ExternalPanelistRepository;
import com.ljy.videoclass.services.rtcConference.application.external.Panelist;
import com.ljy.videoclass.services.rtcConference.model.PanelistId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 해당 회원이 회의를 개최하거나 입장할 수 있는 권한을 갖고있는지 확인하는 validator
 */
@Component
@AllArgsConstructor
public class SimplePermissionValidator implements PermissionValidator {
    private ExternalPanelistRepository panelistRepository;

    public void validation(PanelistId panelistId){
        Panelist panelist = panelistRepository.getPanelist(panelistId.get());
        if(!panelist.isActive()){
            throw new IllegalStateException("회의 개최 및 참여가 불가능한 상태입니다.");
        }
    }
}
