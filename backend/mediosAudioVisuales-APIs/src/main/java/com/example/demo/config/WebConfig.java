package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig {

    // NOTA: Ya no implementamos WebMvcConfigurer.
    // En su lugar, creamos este Bean que Spring Security usará automáticamente.

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 1. Permitir tu Frontend
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        
        // 2. Métodos permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 3. Headers permitidos
        configuration.setAllowedHeaders(List.of("*"));
        
        // 4. Permitir credenciales
        configuration.setAllowCredentials(true);
        
        // 5. Aplicar a TODAS las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}