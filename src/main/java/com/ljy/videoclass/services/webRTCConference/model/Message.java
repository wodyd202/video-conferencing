package com.ljy.videoclass.services.webRTCConference.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {
    private String type;
    private String sender;
    private String receiver;
    private Object data;
    private String conferenceCode;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Message(String type, String sender) {
        this.type = type;
        this.sender = sender;
    }

    public Message(String type, Object data){
        this.type = type;
        this.data = data;
    }

    public Message(String type, String sender, Object data){
        this.type = type;
        this.sender = sender;
        this.data = data;
    }

    public boolean isChat() {
        return type.equals("chat");
    }

    public boolean isShake() {
        return type.equals("shake");
    }
}
