package com.ljy.videoclass.services.elrolment.command.presentation;

import com.ljy.videoclass.services.elrolment.command.application.exception.ElrolmentNotFoundException;
import com.ljy.videoclass.services.elrolment.command.application.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ElrolmentExceptionHandler {

    @ExceptionHandler(ElrolmentNotFoundException.class)
    public ResponseEntity<String> error(ElrolmentNotFoundException error){
        return ResponseEntity.badRequest().body(error.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> error(UserNotFoundException error){
        return ResponseEntity.badRequest().body(error.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> error(IllegalArgumentException error){
        return ResponseEntity.badRequest().body(error.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> error(IllegalStateException error){
        return ResponseEntity.badRequest().body(error.getMessage());
    }
}
