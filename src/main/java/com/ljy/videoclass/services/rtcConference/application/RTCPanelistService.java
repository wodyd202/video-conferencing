package com.ljy.videoclass.services.rtcConference.application;

import com.ljy.videoclass.services.rtcConference.model.PanelistId;
import com.ljy.videoclass.services.rtcConference.model.RtcPanelist;
import com.ljy.videoclass.services.rtcConference.model.SdpInfo;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RTCPanelistService {
    @Autowired private RTCPanelistRegistry panelistRegistry;

    public void receiveVideoFrom(SdpInfo sdpInfo, PanelistId sender, PanelistId panelistId) {
        RtcPanelist rtcPanelist = panelistRegistry.get(panelistId);
        RtcPanelist senderPanelist = panelistRegistry.get(sender);
        rtcPanelist.receiveVideoFrom(senderPanelist, sdpInfo);
        log.info("recevice video from : {}", panelistId);
    }

    public void onIceCandidate(IceCandidate iceCandidate, PanelistId panelistId, PanelistId sender) {
        RtcPanelist rtcPanelist = panelistRegistry.get(panelistId);
        rtcPanelist.addCantidate(iceCandidate, sender);
        log.info("on ice candidate : {}", panelistId);
    }
}
