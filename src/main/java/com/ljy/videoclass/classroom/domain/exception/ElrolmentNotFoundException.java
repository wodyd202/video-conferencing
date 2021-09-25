package com.ljy.videoclass.classroom.domain.exception;

public class ElrolmentNotFoundException extends IllegalArgumentException {
    public ElrolmentNotFoundException(){
        super("수강신청 정보가 존재하지 않습니다.");
    }
}
