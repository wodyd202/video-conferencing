package com.ljy.videoclass.services.rtcConference.model;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.*;
import org.kurento.jsonrpc.JsonUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class RTCPanelist implements Closeable{
    private String panelistId;
    private WebSocketSession session;
    
    private MediaPipeline pipeline;
    private WebRtcEndpoint outgoingMedia;
    private ConcurrentMap<String, WebRtcEndpoint> incomingMedia = new ConcurrentHashMap<>();

    public RTCPanelist(String panelistId,
                       WebSocketSession session,
                       MediaPipeline pipeline) {
        this.panelistId = panelistId;
        this.session = session;
        this.pipeline = pipeline;
        setOutgoingMedia();
    }

    private void setOutgoingMedia() {
        this.outgoingMedia = new WebRtcEndpoint.Builder(pipeline).build();
        this.outgoingMedia.addIceCandidateFoundListener(event -> {
            JsonObject response = new JsonObject();
            response.addProperty("id", "iceCandidate");
            response.addProperty("name", panelistId);
            response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
            sendMessage(response);
        });
    }

    public void cancelVideoFrom(RTCPanelist panelist) {
        final WebRtcEndpoint incoming = incomingMedia.remove(panelist.getPanelistId());
        incoming.release(new Continuation<Void>() {
            @Override
            public void onSuccess(Void result) throws Exception {
                log.trace("PARTICIPANT {}: Released successfully incoming EP for {}",
                        panelist.getPanelistId(), panelist.getPanelistId());
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("PARTICIPANT {}: Could not release incoming EP for {}", panelist.getPanelistId(),
                        panelist.getPanelistId());
            }
        });
    }

    public void receiveVideoFrom(RTCPanelist senderPanelist, String sdpOffer){
        WebRtcEndpoint endpointForPanelist = getEndpointForPanelist(senderPanelist);
        String ipSdpAnswer = endpointForPanelist.processOffer(sdpOffer);

        final JsonObject scParams = new JsonObject();
        scParams.addProperty("id", "receiveVideoAnswer");
        scParams.addProperty("name", senderPanelist.getPanelistId());
        scParams.addProperty("sdpAnswer", ipSdpAnswer);
        sendMessage(scParams);

        getEndpointForPanelist(senderPanelist).gatherCandidates();
    }

    private WebRtcEndpoint getEndpointForPanelist(RTCPanelist panelist) {
        if(this.equals(panelist)){
            return outgoingMedia;
        }
        WebRtcEndpoint incoming = incomingMedia.get(panelist.getPanelistId());
        if(incoming == null){
            incoming = new WebRtcEndpoint.Builder(pipeline).build();
            incoming.addIceCandidateFoundListener(event -> {
                JsonObject response = new JsonObject();
                response.addProperty("id", "iceCandidate");
                response.addProperty("name", panelist.getPanelistId());
                response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                synchronized (session){
                    try {
                        if(session != null && session.isOpen()){
                            session.sendMessage(new TextMessage(response.toString()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
            incomingMedia.put(panelist.getPanelistId(), incoming);
        }
        panelist.getOutgoingWebRtcPeer().connect(incoming);
        return incoming;
    }

    public void addCantidate(IceCandidate iceCandidate, String sender){
        if(panelistId.compareTo(sender) == 0){
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

    public String getPanelistId() {
        return panelistId;
    }

    @Override
    public void close() throws IOException {
        for(String remotePanelistId : incomingMedia.keySet()){
            WebRtcEndpoint ep = this.incomingMedia.get(remotePanelistId);
            ep.release(new Continuation<Void>() {

                @Override
                public void onSuccess(Void result) throws Exception {
                    log.trace("PARTICIPANT {}: Released successfully incoming EP for {}",
                            panelistId, remotePanelistId);
                }

                @Override
                public void onError(Throwable cause) throws Exception {
                    log.warn("PARTICIPANT {}: Could not release incoming EP for {}", panelistId,
                            remotePanelistId);
                }
            });
        }
        outgoingMedia.release(new Continuation<Void>() {

            @Override
            public void onSuccess(Void result) throws Exception {
                log.trace("PARTICIPANT {}: Released outgoing EP", panelistId);
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("USER {}: Could not release outgoing EP", panelistId);
            }
        });
    }
}
