package com.ljy.videoclass.services.conference.command.presentation;

import com.ljy.videoclass.services.conference.command.application.OpenConferenceService;
import com.ljy.videoclass.services.conference.command.model.OpenConference;
import com.ljy.videoclass.services.conference.domain.model.ConferenceModel;
import com.ljy.videoclass.services.conference.domain.value.Creator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

/**
 * 회의 API
 */
@RestController
@RequestMapping("api/conference")
@AllArgsConstructor
public class ConferenceAPI {
    private OpenConferenceService openConferenceService;

    /**
     * @param openConference
     * @param errors
     * 회의 개최
     */
    @PostMapping
    public ResponseEntity<ConferenceModel> open(@Valid @RequestBody OpenConference openConference, Errors errors, Principal principal){
        if(errors.hasErrors()){
            throw new IllegalArgumentException(errors.getAllErrors().get(0).getDefaultMessage());
        }
        ConferenceModel conferenceModel = openConferenceService.open(Creator.of(principal.getName()), openConference);
        return ResponseEntity.ok(conferenceModel);
    }
}
