package com.ljy.videoclass.services.panelist.command.application;

import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.PanelistRepository;
import com.ljy.videoclass.services.panelist.domain.value.PanelistId;
import com.ljy.videoclass.services.rtcConference.application.event.ExpeledPanelistEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class ExpelPanelistService {
    private PanelistRepository panelistRepository;

    @Transactional
    @EventListener
    protected void handle(ExpeledPanelistEvent event){
        PanelistId panelistId = PanelistId.of(event.getTargetId());
        Panelist panelist = panelistRepository.findById(panelistId).get();
        panelist.expell();
        panelistRepository.save(panelist);
    }

}
