package com.ljy.videoclass.services.emailAuth.application;

import com.ljy.videoclass.services.panelist.domain.event.SignUpedPanelist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailAuthEventListener {

    @EventListener
    protected void handle(SignUpedPanelist event){

    }
}
