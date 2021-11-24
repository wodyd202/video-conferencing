package com.ljy.videoclass.services.webRTCConference.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.HashMap;

/**
 * 회의
 */
@Slf4j
public class SocketConference {
    // 회의 코드
    private String conferenceCode;

    // 학생
    private HashMap<String, WebSocketSession> panelistMap;

    public SocketConference(String conferenceCode) {
        this.conferenceCode = conferenceCode;
        panelistMap = new HashMap<>();
    }

    /**
     * @param session
     * @param message
     */
    public void noticeAll(WebSocketSession session, String message) {
        Collection<WebSocketSession> panelists = panelistMap.values();
        for (WebSocketSession webSocketSession : panelists) {
            if(!session.equals(webSocketSession)){
                sendMessage(message, webSocketSession);
            }
        }
    }

    /**
     * @param panelist
     * @param message
     * # 회의자들에게 메시지 보냄
     */
    public void notice(String panelist, String message) {
        sendMessage(message, this.panelistMap.get(panelist));
    }

    private void sendMessage(String message, WebSocketSession webSocketSession) {
        try {
            if (webSocketSession != null && webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage(message));
            }
        } catch (Exception e) {
            log.warn("Error while message sending.", e);
        }
    }

    /**
     * @param session
     * # 회의 참여
     */
    public void join(String panelistId, WebSocketSession session, String message) {
        noticeAll(session, message);
        panelistMap.put(panelistId, session);
    }

    /**
     * @param panelistId
     * # 회의 퇴장
     * @param message
     */
    public void exitAfterNoticeAll(String panelistId, String message) {
        noticeAll(panelistMap.get(panelistId), message);
        panelistMap.remove(panelistId);
    }

    public String getConferenceCode() {
        return conferenceCode;
    }
}
