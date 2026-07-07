package com.example.tompkins.config;

import com.example.tompkins.service.NavigationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public NavigationService navigationService() {
        return new NavigationService();
    }
}
