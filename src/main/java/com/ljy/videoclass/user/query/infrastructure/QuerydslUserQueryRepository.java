package com.ljy.videoclass.user.query.infrastructure;

import com.ljy.videoclass.user.domain.Password;
import com.ljy.videoclass.user.domain.UserId;
import com.ljy.videoclass.user.query.application.UserQueryRepository;
import com.ljy.videoclass.user.domain.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class QuerydslUserQueryRepository implements UserQueryRepository {
    @Autowired private JdbcTemplate jdbcTemplate;

    private final String FIND_LOGIN_INFO_QUERY = "SELECT password, id FROM `users` WHERE id = ?";
    @Override
    public Optional<UserModel> findLoginInfoByUserId(String userId) {
        try{
            return Optional.of(jdbcTemplate.queryForObject(FIND_LOGIN_INFO_QUERY, new RowMapper<UserModel>() {
                @Override
                public UserModel mapRow(ResultSet rs, int i) throws SQLException {
                    return UserModel.builder()
                            .userId(UserId.of(rs.getString("id")))
                            .password(Password.of(rs.getString("password")))
                            .build();
                }
            }, new Object[]{userId}));
        }catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }
}
