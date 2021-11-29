package com.ljy.videoclass.config;

import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class KurentoConfig {
    @Bean
    public KurentoClient kurentoClient() {
        return KurentoClient.create();
    }
}
