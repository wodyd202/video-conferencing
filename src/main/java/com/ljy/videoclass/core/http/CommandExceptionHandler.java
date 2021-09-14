package com.ljy.videoclass.core.http;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CommandExceptionHandler {

    @ExceptionHandler(CommandException.class)
    public ResponseEntity<List<String>> error(CommandException e){
        return ResponseEntity.badRequest().body(e.getErrorMessages());
    }
}