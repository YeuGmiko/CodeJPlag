package com.yeu.code.jplag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${custom.fronted_base_url}")
    private String FRONTED_BASE_URL;


    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                allowedOrigins(FRONTED_BASE_URL.split(";")).
                allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"}).
                allowedHeaders(new String[]{"*"}).
                allowCredentials(true).
                maxAge(3600L);
    }
}
