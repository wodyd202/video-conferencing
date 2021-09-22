package com.ljy.videoclass.classroom.query.presentation;

import com.ljy.videoclass.classroom.domain.exception.*;
import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.domain.value.ClassroomState;
import com.ljy.videoclass.classroom.query.application.ClassroomSearchService;
import com.ljy.videoclass.classroom.query.application.model.ClassroomModels;
import com.ljy.videoclass.classroom.query.application.model.ClassroomSearchModel;
import com.ljy.videoclass.core.http.APIResponse;
import com.ljy.videoclass.core.http.PageRequest;
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
public class ClassroomSearchAPI {
    @Autowired private ClassroomSearchService classroomService;

    @GetMapping("{classroomCode}")
    public APIResponse findByCode(@PathVariable String classroomCode, Principal principal){
        ClassroomModel classroomModel = classroomService.findByCodeAndRegister(classroomCode, principal.getName());
        return new APIResponse(classroomModel, HttpStatus.OK);
    }

    @GetMapping("exist-by")
    public APIResponse findByClassDateAndDayOfWeek(@Valid ClassroomSearchModel classroomSearchModel,
                                                   @Valid PageRequest pageInfo,
                                                   Errors errors,
                                                   Principal principal){
        verifyNotContainsError(errors);
        ClassroomModels classroomModels = classroomService.findByClassDateAndDayOfWeek(principal.getName(), classroomSearchModel, pageInfo);
        return new APIResponse(classroomModels, HttpStatus.OK);
    }

    @GetMapping
    public APIResponse findByRegister(@RequestParam ClassroomState state,
                                      @Valid PageRequest pageRequest,
                                      Errors errors,
                                      Principal principal){
        verifyNotContainsError(errors);
        ClassroomModels classroomModels = classroomService.findByRegister(state, principal.getName(), pageRequest);
        return new APIResponse(classroomModels, HttpStatus.OK);
    }

    @ExceptionHandler({
            ClassroomNotFoundException.class
    })
    public ResponseEntity<String> error(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
