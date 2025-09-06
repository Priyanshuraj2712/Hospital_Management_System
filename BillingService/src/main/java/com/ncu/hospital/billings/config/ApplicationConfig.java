package com.ncu.hospital.billings.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }
    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}

