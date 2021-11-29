package com.ljy.videoclass.services.rtcConference.application;

import com.ljy.videoclass.services.rtcConference.model.RTCConference;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class ConferenceManager {
    @Autowired private KurentoClient kurentoClient;
    private ConcurrentMap<String, RTCConference> conferences = new ConcurrentHashMap<>();

    public Optional<RTCConference> getByCode(String conferenceCode) {
        return Optional.ofNullable(conferences.get(conferenceCode));
    }

    public RTCConference createByCode(String conferenceCode, String panelistId) {
        RTCConference conference = new RTCConference(panelistId, conferenceCode, kurentoClient.createMediaPipeline());
        conferences.put(conferenceCode, conference);
        return conference;
    }
}
