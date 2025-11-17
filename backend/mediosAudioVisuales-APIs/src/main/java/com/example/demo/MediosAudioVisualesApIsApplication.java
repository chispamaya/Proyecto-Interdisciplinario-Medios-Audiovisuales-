package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MediosAudioVisualesApIsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediosAudioVisualesApIsApplication.class, args);
	}
	// --- AGREGAR ESTO PARA ARREGLAR EL ERROR DE CONEXIÓN ---
		@Bean
		public WebMvcConfigurer corsConfigurer() {
			return new WebMvcConfigurer() {
				@Override
				public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/**") // Permite todas las rutas de la API
							.allowedOrigins("http://localhost:5173") // Permite SOLO a tu frontend (chequeá que este sea tu puerto)
							.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // Permite estos métodos
				}
			};
}
}
