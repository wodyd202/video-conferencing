package com.ljy.videoclass.services.conferenceHistory.application;

import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conferenceHistory.domain.ConferenceHistory;
import com.ljy.videoclass.services.conferenceHistory.domain.value.HistoryType;
import com.ljy.videoclass.services.conferenceHistory.domain.value.Payload;
import com.ljy.videoclass.services.rtcConference.application.event.ClosedConferenceEvent;
import com.ljy.videoclass.services.rtcConference.application.event.OpenedConferenceEvent;
import com.ljy.videoclass.services.rtcConference.application.event.OveredMessageCountEvent;
import com.ljy.videoclass.services.rtcConference.application.event.RemovedConferenceMessageStoreEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConferenceHistoryMapper {
    public List<ConferenceHistory> mapFrom(OpenedConferenceEvent openedConferenceEvent) {
        return Arrays.asList(ConferenceHistory.builder()
                .code(ConferenceCode.of(openedConferenceEvent.getConferenceCode()))
                .payload(Payload.of(openedConferenceEvent.getCreatorId()))
                .type(HistoryType.OPEN)
                .build());
    }

    public List<ConferenceHistory> mapFrom(ClosedConferenceEvent closedConferenceEvent) {
        return Arrays.asList(ConferenceHistory.builder()
                .code(ConferenceCode.of(closedConferenceEvent.getConferenceCode()))
                .type(HistoryType.CLOSE)
                .build());
    }

    public List<ConferenceHistory> mapFrom(OveredMessageCountEvent overedMessageCountEvent){
        return overedMessageCountEvent.getChatMessages().stream().map(message->ConferenceHistory.builder()
                .code(ConferenceCode.of(overedMessageCountEvent.getConferenceCode()))
                .payload(createChatPayload(message.getSender(), message.getMessage()))
                .type(HistoryType.CHAT)
                .build()).collect(Collectors.toList());
    }

    public List<ConferenceHistory> mapFrom(RemovedConferenceMessageStoreEvent removedConferenceMessageStoreEvent){
        return removedConferenceMessageStoreEvent.getChatMessages().stream().map(message->ConferenceHistory.builder()
                .code(ConferenceCode.of(removedConferenceMessageStoreEvent.getConferenceCode()))
                .payload(createChatPayload(message.getSender(), message.getMessage()))
                .type(HistoryType.CHAT)
                .build()).collect(Collectors.toList());
    }

    private Payload createChatPayload(String sender, String message){
        return Payload.of("{ sender : \""+sender+"\", message : \"" + message + "\" }");
    }
}
