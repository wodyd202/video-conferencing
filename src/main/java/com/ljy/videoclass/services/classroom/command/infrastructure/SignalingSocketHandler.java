package com.ljy.videoclass.services.classroom.command.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Component
public class SignalingSocketHandler extends TextWebSocketHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SignalingSocketHandler.class);

    private static final String TYPE_INIT = "init";
    private static final String TYPE_LOGOUT = "logout";

    private Map<String, WebSocketSession> connectedUsers = new HashMap<>();
    private Map<String, List<String>> roomMap = new HashMap<>();
    private Map<String, String> userMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.info("[" + session.getId() + "] Connection established " + session.getId());

        final SignalMessage newMenOnBoard = new SignalMessage();
        newMenOnBoard.setType(TYPE_INIT);
        newMenOnBoard.setSender(session.getId());

        String uri = session.getUri().toString();
        int lastSlashIdx = uri.lastIndexOf("/");
        String roomId = uri.substring(lastSlashIdx + 1);

        List<String> room = roomMap.get(roomId);
        if(Objects.isNull(room)){
            room = new ArrayList<>();
            roomMap.put(roomId, room);
        }

        Collection<WebSocketSession> webSocketSessions = connectedUsers.values();
        for (WebSocketSession webSocketSession : webSocketSessions) {
            try {
                if(room.contains(webSocketSession.getId())){
                    webSocketSession.sendMessage(new TextMessage(Utils.getString(newMenOnBoard)));
                }
            } catch (Exception e) {
                LOG.warn("Error while message sending.", e);
            }
        }

        connectedUsers.put(session.getId(), session);
        room.add(session.getId());
        userMap.put(session.getId(), roomId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.info("[" + session.getId() + "] Connection closed " + session.getId() + " with status: " + status.getReason());
        removeUserAndSendLogout(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOG.info("[" + session.getId() + "] Connection error " + session.getId() + " with status: " + exception.getLocalizedMessage());
        removeUserAndSendLogout(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOG.info("handleTextMessage : {}", message.getPayload());

        SignalMessage signalMessage = Utils.getObject(message.getPayload());

        // with the destinationUser find the targeted socket, if any
        String destinationUser = signalMessage.getReceiver();
        WebSocketSession destSocket = connectedUsers.get(destinationUser);
        // if the socket exists and is open, we go on
        if (!Objects.isNull(destSocket) && destSocket.isOpen()) {
            // set the sender as current sessionId.
            signalMessage.setSender(session.getId());
            final String resendingMessage = Utils.getString(signalMessage);
            LOG.info("send message {} to {}", resendingMessage , destinationUser);
            destSocket.sendMessage(new TextMessage(resendingMessage));
        }
    }


    private void removeUserAndSendLogout(final String sessionId) {
        connectedUsers.remove(sessionId);
        String roomId = userMap.get(sessionId);
        List<String> room = roomMap.get(roomId);
        room.remove(sessionId);
        userMap.remove(sessionId);

        // send the message to all other peers, somebody(sessionId) leave.
        final SignalMessage menOut = new SignalMessage();
        menOut.setType(TYPE_LOGOUT);
        menOut.setSender(sessionId);


        connectedUsers.values().forEach(webSocket -> {
            try {
                webSocket.sendMessage(new TextMessage(Utils.getString(menOut)));
            } catch (Exception e) {
                LOG.warn("Error while message sending.", e);
            }
        });
    }
}


