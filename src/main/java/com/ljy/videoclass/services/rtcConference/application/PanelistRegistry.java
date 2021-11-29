package com.ljy.videoclass.services.rtcConference.application;

import com.ljy.videoclass.services.rtcConference.model.RTCPanelist;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class PanelistRegistry {
    private final ConcurrentHashMap<String, RTCPanelist> panelists = new ConcurrentHashMap<>();

    public void register(RTCPanelist panelist){
        panelists.put(panelist.getPanelistId(), panelist);
    }

    public RTCPanelist get(String panelistId){
        return panelists.get(panelistId);
    }

    public RTCPanelist remove(String panelistId) {
        return panelists.remove(panelistId);
    }
}
