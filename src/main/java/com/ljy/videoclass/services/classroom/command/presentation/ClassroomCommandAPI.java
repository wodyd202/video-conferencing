package com.ljy.videoclass.services.classroom.command.presentation;

import com.ljy.videoclass.services.classroom.command.application.*;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassAll;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassInfo;
import com.ljy.videoclass.services.classroom.command.application.model.ChangeClassOptionalDateInfo;
import com.ljy.videoclass.services.classroom.command.application.model.OpenClassroom;
import com.ljy.videoclass.services.classroom.domain.exception.*;
import com.ljy.videoclass.services.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.services.classroom.domain.value.ClassroomCode;
import com.ljy.videoclass.services.classroom.domain.value.Register;
import com.ljy.videoclass.core.http.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.ljy.videoclass.core.http.ControllerHelper.verifyNotContainsError;

/**
 * 수업 관련 API
 */
@RestController
@RequestMapping("api/classroom")
public class ClassroomCommandAPI {
    @Autowired private OpenClassroomService openClassroomService;
    @Autowired private ChangeClassroomService changeClassroomService;

    /**
     * @param openClassroom
     * @param errors
     * @param principal
     * # 수업 개설
     */
    @PostMapping
    public APIResponse open(@Valid @RequestBody OpenClassroom openClassroom, Errors errors, Principal principal){
        verifyNotContainsError(errors);
        ClassroomModel classroomModel = openClassroomService.open(openClassroom, Register.of(principal.getName()));
        return new APIResponse(classroomModel, HttpStatus.OK);
    }

    /**
     * @param classroomCode
     * @param principal
     * # 수업 비활성
     */
    @DeleteMapping("{classroomCode}")
    public APIResponse disable(@PathVariable ClassroomCode classroomCode, Principal principal){
        changeClassroomService.disable(classroomCode, Register.of(principal.getName()));
        return new APIResponse(null, HttpStatus.OK);
    }

    /**
     * @param classroomCode
     * @param principal
     * # 수업 활성
     */
    @PutMapping("{classroomCode}")
    public APIResponse active(@PathVariable ClassroomCode classroomCode, Principal principal){
        changeClassroomService.active(classroomCode, Register.of(principal.getName()));
        return new APIResponse(null, HttpStatus.OK);
    }

    /**
     * @param classroomCode
     * @param changeClassInfo
     * @param errors
     * @param principal
     * # 수업 정보 변경
     */
    @PutMapping("{classroomCode}/class-info")
    public APIResponse changeClassInfo(@PathVariable ClassroomCode classroomCode,
                                       @Valid @RequestBody ChangeClassInfo changeClassInfo,
                                       Errors errors,
                                       Principal principal){
        verifyNotContainsError(errors);
        ClassroomModel classroomModel = changeClassroomService.changeClassInfo(changeClassInfo, classroomCode, Register.of(principal.getName()));
        return new APIResponse(classroomModel, HttpStatus.OK);
    }

    /**
     * @param classroomCode
     * @param changeClassDateInfo
     * @param errors
     * @param principal
     * # 수업 시간 및 요일 정보 변경
     */
    @PutMapping("{classroomCode}/class-date-info")
    public APIResponse changeClassDateInfo(@PathVariable ClassroomCode classroomCode,
                                           @Valid @RequestBody ChangeClassDateInfo changeClassDateInfo,
                                           Errors errors,
                                           Principal principal){
        verifyNotContainsError(errors);
        ClassroomModel classroomModel = changeClassroomService.changeClassDateInfo(changeClassDateInfo, classroomCode, Register.of(principal.getName()));
        return new APIResponse(classroomModel, HttpStatus.OK);
    }

    /**
     * @param classroomCode
     * @param changeClassOptionalDateInfo
     * @param principal
     * # 수업 날짜 정보 변경
     */
    @PutMapping("{classroomCode}/class-optional-date-info")
    public APIResponse changeClassOptioanlDateInfo(@PathVariable ClassroomCode classroomCode,
                                                    @RequestBody(required = false) ChangeClassOptionalDateInfo changeClassOptionalDateInfo,
                                                   Principal principal){
        ClassroomModel classroomModel = changeClassroomService.changeClassOptionalDateInfo(changeClassOptionalDateInfo, classroomCode, Register.of(principal.getName()));
        return new APIResponse(classroomModel, HttpStatus.OK);
    }

    /**
     * @param classroomCode
     * @param changeClassAll
     * @param errors
     * @param principal
     * # 수업 모든 정보 변경
     */
    @PutMapping("{classroomCode}/all")
    public APIResponse changeAll(@PathVariable ClassroomCode classroomCode,
                                 @Valid @RequestBody ChangeClassAll changeClassAll,
                                 Errors errors,
                                 Principal principal){
        verifyNotContainsError(errors);
        ClassroomModel classroomModel = changeClassroomService.changeClassOptionalDateInfo(changeClassAll.getChangeClassOptionalDateInfo(), classroomCode, Register.of(principal.getName()));
        classroomModel = changeClassroomService.changeClassDateInfo(changeClassAll.getChangeClassDateInfo(), classroomCode, Register.of(principal.getName()));
        classroomModel = changeClassroomService.changeClassInfo(changeClassAll.getChangeClassInfo(), classroomCode, Register.of(principal.getName()));
        return new APIResponse(classroomModel, HttpStatus.OK);
    }

    /**
     * @param e
     * @return
     */
    @ExceptionHandler({
            ClassroomNotFoundException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<String> error(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
