package com.example.english_learning_center.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean(name = "jacksonObjectMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}