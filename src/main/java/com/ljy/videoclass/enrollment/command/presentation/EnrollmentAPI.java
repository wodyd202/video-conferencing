package com.ljy.videoclass.enrollment.command.presentation;

import com.ljy.videoclass.core.http.APIResponse;
import com.ljy.videoclass.enrollment.command.application.EnrollmentService;
import com.ljy.videoclass.enrollment.domain.exception.AlreadyErollmentException;
import com.ljy.videoclass.enrollment.domain.exception.ClassroomNotFoundException;
import com.ljy.videoclass.enrollment.domain.exception.NotAllowedEnrollmentException;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.Requester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/classroom/{classroomCode}/enrollment")
public class EnrollmentAPI {
    @Autowired private EnrollmentService enrollmentService;

    @PostMapping
    public APIResponse enrollment(@PathVariable ClassroomCode classroomCode, Principal principal){
        enrollmentService.enrollment(classroomCode, Requester.of(principal.getName()));
        return new APIResponse(null, HttpStatus.OK);
    }

    @ExceptionHandler({
            AlreadyErollmentException.class,
            ClassroomNotFoundException.class,
            NotAllowedEnrollmentException.class
    })
    public APIResponse error(IllegalArgumentException e){
        return new APIResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
