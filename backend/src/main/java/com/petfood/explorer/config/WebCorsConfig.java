package com.petfood.explorer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Enables CORS for the Vite dev server. The allowed origin is externalized
 * (app.cors.allowed-origin, default http://localhost:5173) so it can be changed
 * per environment without touching code.
 */
@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    private final String allowedOrigin;

    public WebCorsConfig(@Value("${app.cors.allowed-origin}") String allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
