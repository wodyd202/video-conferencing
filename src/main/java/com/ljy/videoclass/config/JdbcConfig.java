package com.ljy.videoclass.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {
    @Autowired private DataSource dataSource;
    @Bean
    JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource);
    }
}
