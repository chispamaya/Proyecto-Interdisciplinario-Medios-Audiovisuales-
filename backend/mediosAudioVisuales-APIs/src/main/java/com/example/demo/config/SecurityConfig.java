package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Le decimos a Spring Security que USE la configuración de CORS
            // que ya definiste en tu WebConfig.java
            .cors(withDefaults())
            
            // 2. Deshabilitamos CSRF (Cross-Site Request Forgery).
            // Esto es estándar para APIs REST que no usan cookies de sesión.
            .csrf(csrf -> csrf.disable())
            
            // 3. Autorizamos peticiones
            .authorizeHttpRequests(auth -> auth
                // Permitimos explícitamente las peticiones OPTIONS (pre-vuelo de CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Por ahora, permitimos todo para asegurarnos que el 404 funcione.
                // Más adelante puedes asegurar esto.
                .anyRequest().permitAll()
            );

        return http.build();
    }
}