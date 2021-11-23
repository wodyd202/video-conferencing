package com.ljy.videoclass.services.panelist.query.application;

import com.ljy.videoclass.services.panelist.domain.event.SignUpedPanelist;
import com.ljy.videoclass.services.panelist.domain.model.PanelistModel;
import com.ljy.videoclass.services.panelist.query.infrastructure.RedisQueryPanelistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 회의자 이벤트 리스너
 */
@Slf4j
@Component
public class QueryPanelistEventListener {
    @Autowired
    private RedisQueryPanelistRepository panelistRepository;

    @EventListener
    protected void handle(SignUpedPanelist event){
        PanelistModel panelistModel = new PanelistModel(event);
        panelistRepository.save(panelistModel);
        log.info("save panelist into redis : {}", panelistModel);
    }
}
