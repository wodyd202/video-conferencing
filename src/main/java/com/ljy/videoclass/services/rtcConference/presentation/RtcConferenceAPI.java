package com.ljy.videoclass.services.rtcConference.presentation;

import com.ljy.videoclass.services.rtcConference.application.ConferenceManager;
import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/conference")
@AllArgsConstructor
public class RtcConferenceAPI {
    private ConferenceManager conferenceManager;

    @GetMapping("{conferenceCode}/exist")
    public ResponseEntity<Boolean> existConference(@PathVariable ConferenceCode conferenceCode){
        boolean exist = conferenceManager.existByCode(conferenceCode);
        return ResponseEntity.ok(exist);
    }
}
