package com.example.demo.controller;

import com.example.demo.dto.Errores;
import com.example.demo.service.ErroresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para el recurso 'Errores'.
 * Permite a los administradores consultar la tabla de errores
 * registrados por los Stored Procedures.
 */
@RestController
@RequestMapping("/api/errores") // URL Base: /api/errores
public class ErroresController {

    // 1. Inyecta el servicio de Errores que ya existe
    @Autowired
    private ErroresService erroresService;

    /**
     * Endpoint para OBTENER TODOS los errores registrados en la base de datos.
     * Llama a: erroresService.listarTodosLosErrores()
     * URL: HTTP GET /api/errores
     */
    @GetMapping
    public ResponseEntity<List<Errores>> listarTodosLosErrores() {
        
        // 2. Llama al único método que existe en tu servicio
        List<Errores> listaErrores = erroresService.listarTodosLosErrores();
        
        // 3. Devuelve la lista de errores como JSON
        return ResponseEntity.ok(listaErrores);
    }
}