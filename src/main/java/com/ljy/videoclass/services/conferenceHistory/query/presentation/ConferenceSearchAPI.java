package com.ljy.videoclass.services.conferenceHistory.query.presentation;

import com.ljy.videoclass.services.conferenceHistory.domain.model.ConferenceModel;
import com.ljy.videoclass.services.conferenceHistory.query.application.ConferenceSearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회의 조회 API
 */
@RestController
@RequestMapping("api/conference")
@AllArgsConstructor
public class ConferenceSearchAPI {
    private ConferenceSearchService conferenceSearchService;

    @GetMapping("{code}")
    public ResponseEntity<ConferenceModel> getConference(@PathVariable String code){
        ConferenceModel conferenceModel = conferenceSearchService.getConference(code);
        return ResponseEntity.ok(conferenceModel);
    }
}
