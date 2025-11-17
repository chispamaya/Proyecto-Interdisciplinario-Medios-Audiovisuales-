package com.example.demo.controller;

import com.example.demo.dto.Segmento;

import com.example.demo.dto.SegmentoABMDTO; // El DTO para la tabla
import com.example.demo.service.SegmentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RestController: Define esta clase como un Controller que devuelve JSON.
 * @RequestMapping("/api/segmentos"): URL Base para todos los métodos.
 */
@RestController
@RequestMapping("/api/segmentos")
public class SegmentoController {

    // --- 1. Conectamos el "Cerebro" (Service) ---
    @Autowired
    private SegmentoService segmentoService;

    
    // --- 2. Endpoints (Las URLs de tu API) ---

    /**
     * Endpoint para MOSTRAR la tabla del ABM de Segmentos
     * (Este usa el método de orquestación)
     * - Método HTTP: GET
     * - URL: http://localhost:8080/api/segmentos
     */
    @GetMapping
    public List<SegmentoABMDTO> listarSegmentosParaABM() {
        return segmentoService.listarSegmentosParaABM();
    }

    /**
     * Endpoint para BUSCAR UN Segmento por ID (para llenar el form de "Editar")
     * (Devuelve el DTO 'Segmento' simple, con el idPrograma)
     * - Método HTTP: GET
     * - URL: http://localhost:8080/api/segmentos/1
     */
    @GetMapping("/{id}")
    public Segmento buscarSegmentoPorId(@PathVariable Long id) {
        return segmentoService.buscarSegmentoPorId(id);
    }

    /**
     * Endpoint para CREAR un Segmento (Botón "Agregar" del ABM)
     * - Método HTTP: POST
     * - URL: http://localhost:8080/api/segmentos
     */
    @PostMapping
    public String crearSegmento(@RequestBody Segmento nuevoSegmento) {
        // TODO: Reemplazar '1L' con el ID del usuario logueado (seguridad)
        Long idUsuarioQueCrea = 1L; 
        
        return segmentoService.crearSegmento(nuevoSegmento, idUsuarioQueCrea);
    }

    /**
     * Endpoint para EDITAR un Segmento (Botón "Guardar Cambios")
     * - Método HTTP: PUT
     * - URL: http://localhost:8080/api/segmentos/1
     */
    @PutMapping("/{id}")
    public String modificarSegmento(@PathVariable Long id, @RequestBody Segmento segmento) {
        // Aseguramos que el ID de la URL sea el que se use
        segmento.setId(id); 
        Long idUsuarioQueModifica = 1L; // TODO: Reemplazar con ID de seguridad
        
        return segmentoService.modificarSegmento(segmento, idUsuarioQueModifica);
    }
    
    /**
     * Endpoint para BORRAR un Segmento
     * - Método HTTP: DELETE
     * - URL: http://localhost:8080/api/segmentos/1
     */
    @DeleteMapping("/{id}")
    public String borrarSegmento(@PathVariable Long id) {
        Long idUsuarioQueBorra = 1L; // TODO: Reemplazar con ID de seguridad
        return segmentoService.borrarSegmento(id, idUsuarioQueBorra);
    }
}