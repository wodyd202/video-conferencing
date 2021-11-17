package com.ljy.videoclass.services.classroom.command.infrastructure.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.HashMap;

/**
 * 수업
 */
@Slf4j
public class SocketClassroom {
    // 수업 소유 번호
    private String classroomId;

    // 학생
    private HashMap<String, WebSocketSession> studentMap;

    public SocketClassroom(String classroomId) {
        this.classroomId = classroomId;
        studentMap = new HashMap<>();
    }

    /**
     * @param session
     * @param message
     */
    public void noticeAll(WebSocketSession session, String message) {
        Collection<WebSocketSession> students = studentMap.values();
        for (WebSocketSession webSocketSession : students) {
            if(!session.equals(webSocketSession)){
                sendMessage(message, webSocketSession);
            }
        }
    }

    /**
     * @param student
     * @param message
     * # 학생에게 메시지 보냄
     */
    public void notice(String student, String message) {
        sendMessage(message, this.studentMap.get(student));
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
     * # 수업 참여
     */
    public void join(WebSocketSession session) {
        studentMap.put(session.getId(), session);
    }

    /**
     * @param student
     * # 수업 퇴장
     * @param message
     */
    public void exitAfterNoticeAll(String student, String message) {
        noticeAll(studentMap.get(student), message);
        studentMap.remove(student);
    }

    public String getClassroomId() {
        return classroomId;
    }
}
