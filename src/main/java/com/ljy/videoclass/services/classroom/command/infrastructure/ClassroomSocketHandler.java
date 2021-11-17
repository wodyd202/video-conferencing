package com.ljy.videoclass.services.classroom.command.infrastructure;

import com.ljy.videoclass.services.classroom.command.infrastructure.model.SocketClassroom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

import static com.ljy.videoclass.services.classroom.command.infrastructure.Utils.getString;

@Slf4j
@Component
public class ClassroomSocketHandler extends TextWebSocketHandler {

    // 수업 고유 번호, 수업
    private Map<String, SocketClassroom> classroom = new HashMap<>();

    // 세션 고유번호, 수업
    private Map<String, SocketClassroom> sessionLocation = new HashMap<>();

    /**
     * @param session
     * # 수업 입장
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("[" + session.getId() + "] Connection established " + session.getId());

        Message message = new Message("init", session.getId());

        String classroomId = getClassroomId(session);

        // 수업 정보 가져옴 만약 없으면 새로 생성
        SocketClassroom classroom = this.classroom.getOrDefault(classroomId, new SocketClassroom(classroomId));

        // 수업에 참여하고있는 모든 학생들에게 메시지 보냄
        classroom.noticeAll(session, getString(message));

        // 수업 참여
        classroom.join(session);
        persistClassroom(classroom, session);
    }

    private void persistClassroom(SocketClassroom classroom, WebSocketSession webSocketSession){
        this.classroom.put(classroom.getClassroomId(), classroom);
        sessionLocation.put(webSocketSession.getId(), classroom);
    }

    private String getClassroomId(WebSocketSession session) {
        String uri = session.getUri().toString();
        int lastSlashIdx = uri.lastIndexOf("/");
        String roomId = uri.substring(lastSlashIdx + 1);
        return roomId;
    }

    /**
     * @param session
     * @param textMessage
     * # 메시지 받음
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        log.info("handleTextMessage : {}", textMessage.getPayload());

        Message message = Utils.getObject(textMessage.getPayload());
        message.setSender(session.getId());
        SocketClassroom classroom = this.classroom.get(message.getRoomId());
        if(message.isChat()){
            // 채팅
            classroom.noticeAll(session, getString(message));
        }else{
            // 연결
            classroom.notice(message.getReceiver(), getString(message));
        }
    }

    /**
     * @param sessionId
     * # 소켓 연결 종료
     */
    private void removeUserAndSendLogout(final String sessionId) {
        final Message message = new Message("logout", sessionId);

        SocketClassroom classroom = sessionLocation.get(sessionId);
        sessionLocation.remove(sessionId);

        classroom.exitAfterNoticeAll(sessionId, getString(message));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("[" + session.getId() + "] Connection closed " + session.getId() + " with status: " + status.getReason());
        removeUserAndSendLogout(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.info("[" + session.getId() + "] Connection error " + session.getId() + " with status: " + exception.getLocalizedMessage());
        removeUserAndSendLogout(session.getId());
    }
}


