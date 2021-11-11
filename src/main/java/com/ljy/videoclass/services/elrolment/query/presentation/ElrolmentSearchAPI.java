package com.ljy.videoclass.services.elrolment.query.presentation;

import com.ljy.videoclass.core.http.APIResponse;
import com.ljy.videoclass.core.http.PageRequest;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.ElrolmentState;
import com.ljy.videoclass.services.elrolment.query.application.ElrolmentSearchService;
import com.ljy.videoclass.services.elrolment.query.application.model.ElrolmentModels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.ljy.videoclass.core.http.ControllerHelper.verifyNotContainsError;

@RestController
@RequestMapping("api/elrolment")
public class ElrolmentSearchAPI {
    @Autowired private ElrolmentSearchService elrolmentSearchService;

    @GetMapping
    public APIResponse findByRequester(@RequestParam ElrolmentState state,@Valid PageRequest pageRequest, Errors errors, Principal principal){
        verifyNotContainsError(errors);
        ElrolmentModels elrolmentModels = elrolmentSearchService.findByRegisterAndState(principal.getName(), state, pageRequest);
        return new APIResponse(elrolmentModels, HttpStatus.OK);
    }

    @GetMapping("{classroomCode}")
    public APIResponse findByCodeAndRegister(@PathVariable ClassroomCode classroomCode,
                                             ElrolmentState state,
                                             Principal principal){
        List<ElrolmentModel> elrolments = elrolmentSearchService.findByCodeAndRegisterAndState(classroomCode, principal.getName(), state);
        return new APIResponse(elrolments, HttpStatus.OK);
    }
}
