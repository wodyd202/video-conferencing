package com.ljy.videoclass.services.conferenceHistory.application;

import com.ljy.videoclass.services.conferenceHistory.domain.ConferenceHistory;
import com.ljy.videoclass.services.conferenceHistory.domain.ConferenceHistoryRepository;
import com.ljy.videoclass.services.rtcConference.application.event.ClosedConferenceEvent;
import com.ljy.videoclass.services.rtcConference.application.event.ConferenceEvent;
import com.ljy.videoclass.services.rtcConference.application.event.OpenedConferenceEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@Async("conferenceHistoryExecutor")
public class ConferenceHistoryEventListener {
    private ConferenceHistoryMapper conferenceHistoryMapper;
    private ConferenceHistoryRepository conferenceHistoryRepository;

    private final String MAP_FROM = "mapFrom";
    @EventListener
    protected void handle(ConferenceEvent event) throws Exception {
        Method method = conferenceHistoryMapper.getClass().getDeclaredMethod(MAP_FROM, event.getClass());
        List<ConferenceHistory> conferenceHistory = (List<ConferenceHistory>) method.invoke(conferenceHistoryMapper, event);
        conferenceHistoryRepository.save(conferenceHistory);
        log.info("save conference history into database : {}", conferenceHistory);
    }
}
