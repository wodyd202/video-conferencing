package com.ljy.videoclass.services.conferenceHistory.infrastructure;

import com.ljy.videoclass.services.conferenceHistory.domain.ConferenceHistory;
import com.ljy.videoclass.services.conferenceHistory.domain.ConferenceHistoryRepository;
import com.ljy.videoclass.services.conferenceHistory.domain.model.ConferenceHistoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class JdbcConferenceHistoryRepository implements ConferenceHistoryRepository {
    @Autowired private JdbcTemplate jdbcTemplate;

    private final String SAVE_QUERY = "INSERT INTO conference_history (conference_code, type, payload, create_date_time) values (?, ?, ?, ?)";
    public void save(List<ConferenceHistory> conferenceHistories) {
        jdbcTemplate.batchUpdate(SAVE_QUERY, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                ConferenceHistoryModel conferenceHistory = conferenceHistories.get(i).toModel();
                preparedStatement.setString(1, conferenceHistory.getCode());
                preparedStatement.setString(2, conferenceHistory.getType().toString());
                preparedStatement.setString(3, conferenceHistory.getPayload());
                preparedStatement.setTimestamp(4, Timestamp.valueOf(conferenceHistory.getCreateDateTime()));
            }

            @Override
            public int getBatchSize() {
                return conferenceHistories.size();
            }
        });
    }
}
