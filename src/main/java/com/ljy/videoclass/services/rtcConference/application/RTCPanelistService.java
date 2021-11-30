package com.ljy.videoclass.services.rtcConference.application;

import com.ljy.videoclass.services.rtcConference.model.PanelistId;
import com.ljy.videoclass.services.rtcConference.model.RtcPanelist;
import com.ljy.videoclass.services.rtcConference.model.SdpInfo;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.kurento.client.internal.server.KurentoServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Retryable(maxAttempts = 3, value = KurentoServerException.class, backoff = @Backoff(delay = 500))
public class RTCPanelistService {
    @Autowired private RTCPanelistRegistry panelistRegistry;

    synchronized public void receiveVideoFrom(SdpInfo sdpInfo, PanelistId sender, PanelistId panelistId) {
        RtcPanelist rtcPanelist = panelistRegistry.get(panelistId);
        RtcPanelist senderPanelist = panelistRegistry.get(sender);
        rtcPanelist.receiveVideoFrom(senderPanelist, sdpInfo);
        log.info("recevice video from : {}", panelistId);
    }

    synchronized public void onIceCandidate(IceCandidate iceCandidate, PanelistId panelistId, PanelistId sender) {
        RtcPanelist rtcPanelist = panelistRegistry.get(panelistId);
        rtcPanelist.addCantidate(iceCandidate, sender);
        log.info("on ice candidate : {}", panelistId);
    }
}
