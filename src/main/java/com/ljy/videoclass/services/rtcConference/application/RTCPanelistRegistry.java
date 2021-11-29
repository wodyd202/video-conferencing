package com.ljy.videoclass.services.rtcConference.application;

import com.ljy.videoclass.services.rtcConference.model.PanelistId;
import com.ljy.videoclass.services.rtcConference.model.RtcPanelist;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class RTCPanelistRegistry {
    private final ConcurrentHashMap<PanelistId, RtcPanelist> panelists = new ConcurrentHashMap<>();

    public void register(RtcPanelist panelist){
        panelists.put(panelist.getPanelistId(), panelist);
    }

    public RtcPanelist get(PanelistId panelistId){
        return panelists.get(panelistId);
    }

    public RtcPanelist remove(PanelistId panelistId) {
        return panelists.remove(panelistId);
    }
}
