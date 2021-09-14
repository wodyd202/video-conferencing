package com.ljy.videoclass.classroom.query.presentation;

import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import com.ljy.videoclass.classroom.query.application.ClassroomSearchService;
import com.ljy.videoclass.core.http.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/classroom")
public class ClassroomSearchAPI {
    @Autowired private ClassroomSearchService classroomService;

    @GetMapping
    public APIResponse findByRegister(Principal principal){
        List<ClassroomModel> classroomModels = classroomService.findByRegister(principal.getName());
        return new APIResponse(classroomModels, HttpStatus.OK);
    }
}
