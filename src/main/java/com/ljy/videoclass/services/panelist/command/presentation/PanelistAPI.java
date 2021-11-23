package com.ljy.videoclass.services.panelist.command.presentation;

import com.ljy.videoclass.services.classroom.command.application.model.SignUpPanalist;
import com.ljy.videoclass.services.panelist.command.application.SignUpPanelistService;
import com.ljy.videoclass.services.panelist.domain.model.PanelistModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 회의자 API
 */
@RestController
@RequestMapping("api/panelist")
@AllArgsConstructor
public class PanelistAPI {
    private SignUpPanelistService signUpPanelistService;

    /**
     * @param signUpPanalist
     * @param errors
     * # 회의자 등록
     */
    @PostMapping
    public ResponseEntity<PanelistModel> signUp(@Valid @RequestBody SignUpPanalist signUpPanalist, Errors errors){
        if(errors.hasErrors()){
            throw new IllegalArgumentException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        PanelistModel panelistModel = signUpPanelistService.signUp(signUpPanalist);
        return ResponseEntity.ok(panelistModel);
    }
}
