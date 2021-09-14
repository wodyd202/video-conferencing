package com.ljy.videoclass.enrollment.query.presentation;

import com.ljy.videoclass.core.http.APIResponse;
import com.ljy.videoclass.enrollment.domain.read.EnrollmentModel;
import com.ljy.videoclass.enrollment.domain.value.ClassroomCode;
import com.ljy.videoclass.enrollment.domain.value.EnrollmentState;
import com.ljy.videoclass.enrollment.query.application.EnrollmentSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/classroom/{classroomCode}/enrollment")
public class EnrollmentQueryAPI {
    @Autowired private EnrollmentSearchService enrollmentService;

    @GetMapping("request")
    public APIResponse findByCode(@PathVariable ClassroomCode classroomCode, @RequestParam EnrollmentState state, Principal principal) {
        List<EnrollmentModel> enrollmentModels = enrollmentService.findByCodeAndRegister(classroomCode, state, principal.getName());
        return new APIResponse(enrollmentModels, HttpStatus.OK);
    }
}
