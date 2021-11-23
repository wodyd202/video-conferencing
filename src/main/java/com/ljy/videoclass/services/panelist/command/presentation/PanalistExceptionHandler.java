package com.ljy.videoclass.services.panelist.command.presentation;

import com.ljy.videoclass.services.panelist.domain.exception.AlreadyExistPanelistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PanalistExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> error(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AlreadyExistPanelistException.class)
    public ResponseEntity<String> error(AlreadyExistPanelistException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
