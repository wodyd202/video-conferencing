package com.ljy.videoclass.services.conferenceHistory.domain.model;

import com.ljy.videoclass.services.conferenceHistory.domain.value.ConferenceCode;
import com.ljy.videoclass.services.conferenceHistory.domain.value.HistoryType;
import com.ljy.videoclass.services.conferenceHistory.domain.value.Payload;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConferenceHistoryModel {
    private String code;
    private HistoryType type;
    private String payload;
    private LocalDateTime createDateTime;

    @Builder
    public ConferenceHistoryModel(ConferenceCode code,
                                  HistoryType type,
                                  Payload payload,
                                  LocalDateTime createDateTime) {
        this.code = code.get();
        this.type = type;
        if(payload != null){
            this.payload = payload.get();
        }
        this.createDateTime = createDateTime;
    }
}
