package com.ljy.videoclass.config;

import com.ljy.videoclass.classroom.command.infrastructure.SignalingSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketsConfiguration implements WebSocketConfigurer {

    @Autowired private SignalingSocketHandler signalingSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(signalingSocketHandler,"/class/**");
    }
}
