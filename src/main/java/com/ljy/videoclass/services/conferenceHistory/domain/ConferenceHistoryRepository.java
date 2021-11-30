package com.ljy.videoclass.services.conferenceHistory.domain;

import java.util.List;

public interface ConferenceHistoryRepository {
    void save(List<ConferenceHistory> conferenceHistories);
}
