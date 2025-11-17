package com.example.demo.controller;

import com.example.demo.dto.Plataforma;

import com.example.demo.service.PlataformaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RestController: Define esta clase como un Controller que devuelve JSON.
 * @RequestMapping("/api/plataformas"): URL Base para todos los métodos.
 */
@RestController
@RequestMapping("/api/plataformas")
public class PlataformaController {

    // --- 1. Conectamos el "Cerebro" (Service) ---
    @Autowired
    private PlataformaService plataformaService;

    
    // --- 2. Endpoints (Las URLs de tu API) ---

    /**
     * Endpoint para MOSTRAR la tabla del ABM de Plataformas
     * - Método HTTP: GET
     * - URL: http://localhost:8080/api/plataformas
     */
    @GetMapping
    public List<Plataforma> listarPlataformas() {
        return plataformaService.listarTodasLasPlataformas();
    }

    /**
     * Endpoint para BUSCAR UNA Plataforma por ID (para llenar el form de "Editar")
     * - Método HTTP: GET
     * - URL: http://localhost:8080/api/plataformas/1
     */
    @GetMapping("/{id}")
    public Plataforma buscarPlataformaPorId(@PathVariable Long id) {
        return plataformaService.buscarPlataformaPorId(id);
    }

    /**
     * Endpoint para CREAR una Plataforma (Botón "Agregar" del ABM)
     * - Método HTTP: POST
     * - URL: http://localhost:8080/api/plataformas
     */
    @PostMapping
    public String crearPlataforma(@RequestBody Plataforma nuevaPlataforma) {
        // TODO: Reemplazar '1L' con el ID del usuario logueado (seguridad)
        Long idUsuarioQueCrea = 1L; 
        
        return plataformaService.crearPlataforma(nuevaPlataforma, idUsuarioQueCrea);
    }

    /**
     * Endpoint para EDITAR una Plataforma
     * - Método HTTP: PUT
     * - URL: http://localhost:8080/api/plataformas/1
     */
    @PutMapping("/{id}")
    public String modificarPlataforma(@PathVariable Long id, @RequestBody Plataforma plataforma) {
        // Aseguramos que el ID de la URL sea el que se use
        plataforma.setId(id); 
        Long idUsuarioQueModifica = 1L; // TODO: Reemplazar con ID de seguridad
        
        return plataformaService.modificarPlataforma(plataforma, idUsuarioQueModifica);
    }
    
    /**
     * Endpoint para BORRAR una Plataforma
     * - Método HTTP: DELETE
     * - URL: http://localhost:8080/api/plataformas/1
     */
    @DeleteMapping("/{id}")
    public String borrarPlataforma(@PathVariable Long id) {
        Long idUsuarioQueBorra = 1L; // TODO: Reemplazar con ID de seguridad
        return plataformaService.borrarPlataforma(id, idUsuarioQueBorra);
    }
}