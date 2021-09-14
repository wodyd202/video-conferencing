package com.ljy.videoclass.classroom.command.presentation;

import com.ljy.videoclass.classroom.command.application.*;
import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.ChangeClassInfo;
import com.ljy.videoclass.classroom.domain.OpenClassroom;
import com.ljy.videoclass.classroom.domain.exception.*;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.classroom.domain.value.Register;
import com.ljy.videoclass.core.http.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.ljy.videoclass.core.http.ControllerHelper.verifyNotContainsError;

@RestController
@RequestMapping("api/classroom")
public class ClassroomCommandAPI {
    @Autowired private OpenClassroomService openClassroomService;
    @Autowired private DisableClassroomService disableClassroomService;
    @Autowired private ActiveClassroomService activeClassroomService;
    @Autowired private ChangeClassroomInfoService changeClassroomInfoService;
    @Autowired private ChangeClassDateInfoService changeClassDateInfoService;

    @PostMapping
    public APIResponse open(@Valid @RequestBody OpenClassroom openClassroom, Errors errors, Principal principal){
        verifyNotContainsError(errors);
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of(principal.getName()));
        return new APIResponse(classroomModel, HttpStatus.OK);
    }

    @DeleteMapping("{classroomCode}")
    public APIResponse disable(@PathVariable ClassroomCode classroomCode, Principal principal){
        disableClassroomService.disable(classroomCode, Register.of(principal.getName()));
        return new APIResponse(null, HttpStatus.OK);
    }

    @PutMapping("{classroomCode}")
    public APIResponse active(@PathVariable ClassroomCode classroomCode, Principal principal){
        activeClassroomService.active(classroomCode, Register.of(principal.getName()));
        return new APIResponse(null, HttpStatus.OK);
    }

    @PutMapping("{classroomCode}/class-info")
    public APIResponse changeClassInfo(@PathVariable ClassroomCode classroomCode,
                                       @Valid @RequestBody ChangeClassInfo changeClassInfo,
                                       Errors errors,
                                       Principal principal){
        verifyNotContainsError(errors);
        ClassroomModel classroomModel = changeClassroomInfoService.changeClassInfo(changeClassInfo, classroomCode, Register.of(principal.getName()));
        return new APIResponse(classroomModel, HttpStatus.OK);
    }

    @PutMapping("{classroomCode}/class-date-info")
    public APIResponse changeClassDateInfo(@PathVariable ClassroomCode classroomCode,
                                           @Valid @RequestBody ChangeClassDateInfo changeClassDateInfo,
                                           Errors errors,
                                           Principal principal){
        verifyNotContainsError(errors);
        ClassroomModel classroomModel = changeClassDateInfoService.changeClassDateInfo(changeClassDateInfo, classroomCode, Register.of(principal.getName()));
        return new APIResponse(classroomModel, HttpStatus.OK);
    }

    @ExceptionHandler({
            InvalidClassDateInfoException.class,
            InvalidClassTitleException.class,
            ClassTimeOverlapException.class,
            ClassroomNotFoundException.class,
            AlreadyDisabledClassException.class
    })
    public ResponseEntity<String> error(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
