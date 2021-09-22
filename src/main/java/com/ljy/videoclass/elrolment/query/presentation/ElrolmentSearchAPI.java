package com.ljy.videoclass.elrolment.query.presentation;

import com.ljy.videoclass.classroom.command.application.model.ElrolmentModel;
import com.ljy.videoclass.core.http.APIResponse;
import com.ljy.videoclass.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.elrolment.query.application.ElrolmentSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/elrolment/{classroomCode}")
public class ElrolmentSearchAPI {
    @Autowired private ElrolmentSearchService elrolmentSearchService;

    @GetMapping
    public APIResponse findByCodeAndRegister(@PathVariable ClassroomCode classroomCode, Principal principal){
        List<ElrolmentModel> elrolments = elrolmentSearchService.findByCodeAndRegister(classroomCode, principal.getName());
        return new APIResponse(elrolments, HttpStatus.OK);
    }
}
