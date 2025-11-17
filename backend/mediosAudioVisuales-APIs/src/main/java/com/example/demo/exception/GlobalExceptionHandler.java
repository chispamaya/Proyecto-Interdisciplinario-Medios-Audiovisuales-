package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException; // Para el 404

import jakarta.servlet.http.HttpServletRequest; // Para obtener la URL

/**
 * @ControllerAdvice
 * Le dice a Spring que esta clase vigilará a TODOS los @RestControllers.
 * Interceptará excepciones y devolverá un JSON personalizado.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manejador para errores 404 (Ruta No Encontrada).
     * Se activa cuando alguien intenta ir a una URL que no existe
     * (ej: /api/usuariosss).
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex, HttpServletRequest httpRequest) {
        
        HttpStatus status = HttpStatus.NOT_FOUND; // 404

        ErrorResponse errorResponse = new ErrorResponse(
            status.value(),                 // 404
            status.getReasonPhrase(),       // "Not Found"
            "La ruta solicitada no existe.", // Mensaje amigable
            httpRequest.getRequestURI()     // La URL que falló
        );
        
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Manejador para errores 500 (Error Interno del Servidor).
     * Se activa para CUALQUIER OTRA excepción que no manejemos
     * (ej: NullPointerException, SQLException, etc.)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest httpRequest) {
        
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        ErrorResponse errorResponse = new ErrorResponse(
            status.value(),                 // 500
            status.getReasonPhrase(),       // "Internal Server Error"
            ex.getMessage(),                // El mensaje técnico del error
            httpRequest.getRequestURI()     // La URL que falló
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}