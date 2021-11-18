package com.ljy.videoclass.services.classroom.command.infrastructure;

import com.ljy.videoclass.services.classroom.command.application.ChatMessageRepository;
import com.ljy.videoclass.services.classroom.command.infrastructure.model.ChatMessage;
import com.ljy.videoclass.services.classroom.command.infrastructure.model.SocketClassroom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.*;

import static com.ljy.videoclass.services.classroom.command.infrastructure.Utils.getString;

@Slf4j
@Component
public class ClassroomSocketHandler extends TextWebSocketHandler {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // 수업 고유 번호, 수업
    private Map<String, SocketClassroom> classroom = new HashMap<>();

    // 사용자 아이디, 수업
    private Map<String, SocketClassroom> studentLocation = new HashMap<>();

    /**
     * @param session
     * # 수업 입장
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = session.getPrincipal().getName();
        log.info("[" + userId + "] Connection established " + userId);

        Message message = new Message("init", userId);

        // url로 부터 수업 코드를 가져옴
        String classroomId = getClassroomId(session);

        // 수업 정보 가져옴 만약 없으면 새로 생성
        SocketClassroom classroom = this.classroom.getOrDefault(classroomId, new SocketClassroom(classroomId));

        // 수업에 참여하고있는 모든 학생들에게 메시지 보냄
        classroom.noticeAll(session, getString(message));

        // 수업 참여
        classroom.join(userId, session, getString(new Message("join", userId)));
        persistIntoClassroom(userId, classroom);

        // 입장한 학생에게 이전 대화내용 주기
        sendBeforeChatMessages(classroom, classroomId, userId);
    }

    private void persistIntoClassroom(String userId, SocketClassroom classroom){
        this.classroom.put(classroom.getClassroomId(), classroom);
        studentLocation.put(userId, classroom);
    }

    private String getClassroomId(WebSocketSession session) {
        String uri = session.getUri().toString();
        int lastSlashIdx = uri.lastIndexOf("/");
        String roomId = uri.substring(lastSlashIdx + 1);
        return roomId;
    }

    private void sendBeforeChatMessages(SocketClassroom socketClassroom, String classroomId, String userId){
        Message message = new Message("enter", "classroomMaster", chatMessageRepository.findAll(classroomId));
        socketClassroom.notice(userId, getString(message));
    }

    /**
     * @param session
     * @param textMessage
     * # 메시지 받음
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        String userId = session.getPrincipal().getName();
        log.info("handleTextMessage : {}", textMessage.getPayload());

        Message message = Utils.getObject(textMessage.getPayload());
        message.setSender(userId);

        SocketClassroom classroom = this.classroom.get(message.getRoomId());
        if(message.isChat()){
            // 채팅
            classroom.noticeAll(session, getString(message));

            // 이전 메시지에 등록
            ChatMessage chatMessage = ChatMessage.builder()
                    .localDateTime(LocalDateTime.now())
                    .message(message.getData().toString())
                    .sender(userId)
                    .build();
            chatMessageRepository.save(message.getRoomId(), chatMessage);
        }else if(message.isShake()){
            // 흔들기
            classroom.noticeAll(session, getString(message));
        }else{
            // 연결
            classroom.notice(message.getReceiver(), getString(message));
        }
    }

    /**
     * @param userId
     * # 소켓 연결 종료
     */
    private void removeUserAndSendLogout(String userId) {
        final Message message = new Message("logout", userId);

        SocketClassroom classroom = studentLocation.get(userId);
        studentLocation.remove(userId);

        classroom.exitAfterNoticeAll(userId, getString(message));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = session.getPrincipal().getName();
        log.info("[" + userId + "] Connection closed " + userId + " with status: " + status.getReason());
        removeUserAndSendLogout(userId);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        String userId = session.getPrincipal().getName();
        log.info("[" + userId + "] Connection error " + userId + " with status: " + exception.getLocalizedMessage());
        removeUserAndSendLogout(userId);
    }
}


