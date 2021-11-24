package com.ljy.videoclass.services.webRTCConference;

import com.ljy.videoclass.services.webRTCConference.model.ChatMessage;
import com.ljy.videoclass.services.webRTCConference.model.Message;
import com.ljy.videoclass.services.webRTCConference.model.SocketConference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.*;

import static com.ljy.videoclass.services.webRTCConference.Utils.getString;

@Slf4j
@Component
public class ConferenceSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // 수업 고유 번호, 수업
    private Map<String, SocketConference> conferences = new HashMap<>();

    // 사용자 아이디, 수업
    private Map<String, SocketConference> panelistLocation = new HashMap<>();

    /**
     * @param session
     * # 수업 입장
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String panelistId = session.getPrincipal().getName();
        log.info("[" + panelistId + "] Connection established " + panelistId);

        Message message = new Message("init", panelistId);

        // url로 부터 수업 코드를 가져옴
        String conferenceCode = getConferenceCode(session);

        // 회의 정보 가져옴 만약 없으면 새로 생성
        SocketConference conference = this.conferences.getOrDefault(conferenceCode, new SocketConference(conferenceCode));

        // 회의에 참여하고있는 모든 학생들에게 메시지 보냄
        conference.noticeAll(session, getString(message));

        // 회의 참여
        conference.join(panelistId, session, getString(new Message("join", panelistId)));
        persistIntoClassroom(panelistId, conference);

        // 입장한 회의자에게 이전 대화내용 주기
        sendBeforeChatMessages(conference, conferenceCode, panelistId);
    }

    private void persistIntoClassroom(String userId, SocketConference classroom){
        this.conferences.put(classroom.getConferenceCode(), classroom);
        panelistLocation.put(userId, classroom);
    }

    private String getConferenceCode(WebSocketSession session) {
        String uri = session.getUri().toString();
        int lastSlashIdx = uri.lastIndexOf("/");
        String conferenceCode = uri.substring(lastSlashIdx + 1);
        return conferenceCode;
    }

    private void sendBeforeChatMessages(SocketConference socketClassroom, String classroomId, String userId){
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
        String panelistId = session.getPrincipal().getName();
        log.info("handleTextMessage : {}", textMessage.getPayload());

        Message message = Utils.getObject(textMessage.getPayload());
        message.setSender(panelistId);

        SocketConference classroom = this.conferences.get(message.getConferenceCode());
        if(message.isChat()){
            // 채팅
            classroom.noticeAll(session, getString(message));

            // 이전 메시지에 등록
            ChatMessage chatMessage = ChatMessage.builder()
                    .localDateTime(LocalDateTime.now())
                    .message(message.getData().toString())
                    .sender(panelistId)
                    .build();
            chatMessageRepository.save(message.getConferenceCode(), chatMessage);
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

        SocketConference classroom = panelistLocation.get(userId);
        panelistLocation.remove(userId);

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


