package com.ljy.videoclass.services.rtcConference.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class ChatMessage implements Serializable {
    // 보낸이
    private String sender;

    // 메시지
    private String message;

    // 작성일자
    private LocalDateTime sendDate;

    @Builder
    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.sendDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", localDateTime=" + sendDate +
                '}';
    }
}
