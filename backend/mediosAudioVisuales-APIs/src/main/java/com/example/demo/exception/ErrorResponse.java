package com.example.demo.exception;

import java.time.LocalDateTime;

// No usamos @Data de Lombok para ser explícitos
public class ErrorResponse {
    
    private int status; // El código HTTP (ej: 404, 500)
    private String error; // El nombre del error (ej: "Not Found")
    private String message; // El mensaje del error (ej: "NullPointerException")
    private String path; // La URL que falló
    private LocalDateTime timestamp; // Cuándo ocurrió

    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    // Getters (necesarios para que Spring los convierta a JSON)
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
    public LocalDateTime getTimestamp() { return timestamp; }
}