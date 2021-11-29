package com.ljy.videoclass.services.rtcConference.application;

import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
import com.ljy.videoclass.services.rtcConference.model.PanelistId;
import com.ljy.videoclass.services.rtcConference.model.RtcConference;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
@Profile("!test")
public class ConferenceManager {
    @Autowired private KurentoClient kurentoClient;
    private ConcurrentMap<ConferenceCode, RtcConference> conferences = new ConcurrentHashMap<>();

    public Optional<RtcConference> getByCode(ConferenceCode conferenceCode) {
        return Optional.ofNullable(conferences.get(conferenceCode));
    }

    public RtcConference createByCode(ConferenceCode conferenceCode, PanelistId panelistId) {
        RtcConference conference = new RtcConference(panelistId, conferenceCode, kurentoClient.createMediaPipeline());
        conferences.put(conferenceCode, conference);
        return conference;
    }

    public void remove(RtcConference conference) {
        conferences.remove(conference.getCode());
    }
}
