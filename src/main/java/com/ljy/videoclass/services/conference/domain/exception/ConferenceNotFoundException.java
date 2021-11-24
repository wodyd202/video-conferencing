package com.ljy.videoclass.services.conference.domain.exception;

public class ConferenceNotFoundException extends RuntimeException {
    public ConferenceNotFoundException(){
        super("해당 회의 정보가 존재하지 않습니다.");
    }
}
