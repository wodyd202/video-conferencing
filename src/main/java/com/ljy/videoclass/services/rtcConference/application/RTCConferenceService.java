package com.ljy.videoclass.services.rtcConference.application;

import com.ljy.videoclass.services.rtcConference.application.event.ClosedConferenceEvent;
import com.ljy.videoclass.services.rtcConference.application.event.ExpeledPanelistEvent;
import com.ljy.videoclass.services.rtcConference.application.event.OpenedConferenceEvent;
import com.ljy.videoclass.services.rtcConference.application.exception.ConferenceNotFoundException;
import com.ljy.videoclass.services.rtcConference.application.exception.NotMatchKeyException;
import com.ljy.videoclass.services.rtcConference.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

/**
 *  화상회의 서비스
 */
@Slf4j
@Service
@Profile("!test")
@AllArgsConstructor
public class RTCConferenceService {
    private ConferenceManager conferenceManager;
    private RTCPanelistRegistry panelistRegistry;
    private RTCChatService chatService;

    private PermissionValidator permissionValidator;

    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * @param openerId
     * @param conferenceCode
     * @param openerSession
     */
    public void open(PanelistId openerId, ConferenceCode conferenceCode, WebSocketSession openerSession) {
        RtcConference conference = conferenceManager.createByCode(conferenceCode);
        RtcPanelist panelist = conference.getCreator(permissionValidator, openerId, openerSession);

        conference.open(permissionValidator, panelist);

        applicationEventPublisher.publishEvent(new OpenedConferenceEvent(conferenceCode, openerId));

        panelistRegistry.register(panelist);
        log.info("open conference : ", conferenceCode);
    }

    private ConferenceCode createConferenceCode() {
        return ConferenceCode.of(UUID.randomUUID().toString());
    }

    /**
     * @param conferenceCode 회의 코드
     * @param joinerId 참여자 아이디
     * @param key
     * @param joinerSession 참여자 세션
     * # 회의 참여
     */
    synchronized public void join(ConferenceCode conferenceCode,
                                  PanelistId joinerId,
                                  ConferenceKey key,
                                  WebSocketSession joinerSession) throws NotMatchKeyException, ConferenceNotFoundException {
        // 회의 정보 가져옴
        RtcConference conference = getConference(conferenceCode);

        // 회의로 부터 회의 참여 권한을 생성
        RtcPanelist panelist = conference.getPermissionToParticipate(permissionValidator, joinerId, key, joinerSession);

        // 회의 참여
        conference.join(panelist);

        // 회의 참여중인 인원에 추가
        panelistRegistry.register(panelist);
        log.info("join panelist into {} conference : ", conferenceCode);

        // 이전 메시지 전송
        chatService.sendBeforeMessage(conference, joinerId);
    }

    /**
     * @param conferenceCode 회의 코드
     * @param leaverId 퇴장자 아이디
     */
    synchronized public void leave(ConferenceCode conferenceCode, PanelistId leaverId) throws ConferenceNotFoundException {
        // 회의 정보 가져옴
        RtcConference conference = getConference(conferenceCode);
        RtcPanelist rtcPanelist = panelistRegistry.get(leaverId);

        // 퇴장
        conference.leave(rtcPanelist);

        panelistRegistry.remove(leaverId);
        log.info("{} leave the {} conference", leaverId, conferenceCode);

        // 회의자가 아무도 존재하지 않을때 회의방 제거
        if(conference.isClose()){
            conferenceManager.remove(conference);
            applicationEventPublisher.publishEvent(new ClosedConferenceEvent(conferenceCode));
        }
    }

    private RtcConference getConference(ConferenceCode conferenceCode) throws ConferenceNotFoundException {
        return conferenceManager.getByCode(conferenceCode).orElseThrow(ConferenceNotFoundException::new);
    }

    /**
     * @param conferenceCode
     * @param target
     * @param who
     * # 회의자 추방
     */
    public void expel(ConferenceCode conferenceCode, PanelistId target, PanelistId who) throws ConferenceNotFoundException {
        // 회의 정보 가져옴
        RtcConference conference = getConference(conferenceCode);

        // 추방
        conference.expel(panelistRegistry.get(target), panelistRegistry.get(who));

        applicationEventPublisher.publishEvent(new ExpeledPanelistEvent(target));
    }

    /**
     * @param conferenceCode
     * # 흔들기
     */
    public void shake(ConferenceCode conferenceCode, PanelistId who) throws ConferenceNotFoundException {
        // 회의 정보 가져옴
        RtcConference conference = getConference(conferenceCode);

        // 흔들기
        conference.shake(panelistRegistry.get(who));
    }

    /**
     * @param conferenceCode
     * @param chatMessage
     */
    public void sendChatMessage(ConferenceCode conferenceCode, ChatMessage chatMessage) throws ConferenceNotFoundException {
        // 회의 정보 가져옴
        RtcConference conference = getConference(conferenceCode);

        conference.sendChat(chatMessage);

        // 메시지 저장
        chatService.persist(conferenceCode, chatMessage);
    }
}
