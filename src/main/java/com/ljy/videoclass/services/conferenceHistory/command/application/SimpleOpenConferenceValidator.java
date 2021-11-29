package com.ljy.videoclass.services.conferenceHistory.command.application;

import com.ljy.videoclass.services.conferenceHistory.command.application.exception.PanelistNotFoundException;
import com.ljy.videoclass.services.conferenceHistory.command.application.external.ExternalPanelistRepository;
import com.ljy.videoclass.services.conferenceHistory.domain.OpenConferenceValidator;
import com.ljy.videoclass.services.conferenceHistory.domain.value.Creator;
import com.ljy.videoclass.services.panelist.command.application.model.Panelist;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimpleOpenConferenceValidator implements OpenConferenceValidator {
    private ExternalPanelistRepository panelistRepository;

    @Override
    public void validation(Creator creator) {
        Panelist panelist = panelistRepository.getPanelist(creator.get()).orElseThrow(PanelistNotFoundException::new);
        if(panelist.isNotActive()){
            throw new IllegalStateException("정지된 회의자는 회의를 개최할 수 없습니다.");
        }
    }
}
