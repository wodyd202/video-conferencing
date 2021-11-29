package com.ljy.videoclass.services.rtcConference.model;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.*;
import org.kurento.jsonrpc.JsonUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.Closeable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class RtcPanelist implements Closeable {
    private PanelistId panelistId;
    private WebSocketSession session;
    
    private MediaPipeline pipeline;

    // WebRtcEndpoint
    private WebRtcEndpoint outgoingMedia;
    private ConcurrentMap<PanelistId, WebRtcEndpoint> incomingMedia = new ConcurrentHashMap<>();

    public RtcPanelist(PanelistId panelistId,
                       WebSocketSession session,
                       MediaPipeline pipeline) {
        this.panelistId = panelistId;
        this.session = session;
        this.pipeline = pipeline;
        setOutgoingMedia();
    }

    private void setOutgoingMedia() {
        this.outgoingMedia = new WebRtcEndpoint.Builder(pipeline).build();
        this.outgoingMedia.addIceCandidateFoundListener(event -> sendMessage(createIceCandidateJson(panelistId, event)));
    }

    private JsonObject createIceCandidateJson(PanelistId panelistId, IceCandidateFoundEvent event){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "iceCandidate");
        jsonObject.addProperty("name", panelistId.get());
        jsonObject.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
        return jsonObject;
    }

    public void cancelVideoFrom(RtcPanelist panelist) {
        final WebRtcEndpoint incoming = incomingMedia.remove(panelist.getPanelistId());
        incoming.release();
    }

    public void receiveVideoFrom(RtcPanelist senderPanelist, SdpInfo sdpOffer){
        WebRtcEndpoint endpointForPanelist = getEndpointForPanelist(senderPanelist);
        String ipSdpAnswer = endpointForPanelist.processOffer(sdpOffer.getSdp());

        sendMessage(createReceiveVideoAnswerJson(senderPanelist, ipSdpAnswer));

        getEndpointForPanelist(senderPanelist).gatherCandidates();
    }

    private JsonObject createReceiveVideoAnswerJson(RtcPanelist senderPanelist, String ipSdpAnswer){
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "receiveVideoAnswer");
        jsonObject.addProperty("name", senderPanelist.getPanelistId().get());
        jsonObject.addProperty("sdpAnswer", ipSdpAnswer);
        return jsonObject;
    }

    private WebRtcEndpoint getEndpointForPanelist(RtcPanelist panelist) {
        if(this.equals(panelist)){
            return outgoingMedia;
        }
        WebRtcEndpoint incoming = incomingMedia.get(panelist.getPanelistId());
        if(incoming == null){
            incoming = new WebRtcEndpoint.Builder(pipeline).build();
            incoming.addIceCandidateFoundListener(event -> createIceCandidateJson(panelist.getPanelistId(), event));
            incomingMedia.put(panelist.getPanelistId(), incoming);
        }
        panelist.getOutgoingWebRtcPeer().connect(incoming);
        return incoming;
    }

    public void addCantidate(IceCandidate iceCandidate, PanelistId sender){
        if(panelistId.get().compareTo(sender.get()) == 0){
            outgoingMedia.addIceCandidate(iceCandidate);
        }else{
            WebRtcEndpoint webRtcEndpoint = incomingMedia.get(sender);
            if(webRtcEndpoint != null){
                webRtcEndpoint.addIceCandidate(iceCandidate);
            }
        }
    }

    private WebRtcEndpoint getOutgoingWebRtcPeer() {
        return outgoingMedia;
    }

    public void sendMessage(JsonObject message) {
        synchronized(session){
            try{
                if(session != null && session.isOpen()){
                    session.sendMessage(new TextMessage(message.toString()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public PanelistId getPanelistId() {
        return panelistId;
    }

    @Override
    public void close() {
        for(PanelistId remotePanelistId : incomingMedia.keySet()){
            WebRtcEndpoint ep = this.incomingMedia.get(remotePanelistId);
            ep.release();
        }
        outgoingMedia.release();
    }
}
