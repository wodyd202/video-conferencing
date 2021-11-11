package com.ljy.videoclass.services.classroom.domain.exception;

public class ClassroomNotFoundException extends IllegalArgumentException{
    public ClassroomNotFoundException(){
        super("해당 수업이 존재하지 않습니다.");
    }
}
