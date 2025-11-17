package com.example.demo.controller;

import com.example.demo.dto.Tag;
import com.example.demo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RestController: Define esta clase como un Controller que devuelve JSON.
 * @RequestMapping("/api/tags"): URL Base para todos los métodos.
 */
@RestController
@RequestMapping("/api/tags")
public class TagController {

    // --- 1. Conectamos el "Cerebro" (Service) ---
    @Autowired
    private TagService tagService;

    
    // --- 2. Endpoints (Las URLs de tu API) ---

    /**
     * Endpoint para MOSTRAR la lista de todos los Tags
     * - Método HTTP: GET
     * - URL: http://localhost:8080/api/tags
     */
    @GetMapping
    public List<Tag> listarTodosLosTags() {
        return tagService.listarTodosLosTags();
    }

    /**
     * Endpoint para BUSCAR UN Tag por ID
     * - Método HTTP: GET
     * - URL: http://localhost:8080/api/tags/1
     */
    @GetMapping("/{id}")
    public Tag buscarTagPorId(@PathVariable Long id) {
        return tagService.buscarTagPorId(id);
    }

    /**
     * Endpoint para CREAR un Tag (SP 'ct')
     * - Método HTTP: POST
     * - URL: http://localhost:8080/api/tags
     */
    @PostMapping
    public String crearTag(@RequestBody Tag nuevoTag) {
        // TODO: Reemplazar '1L' con el ID del usuario logueado (seguridad)
        Long idUsuarioQueCrea = 1L; 
        
        return tagService.crearTag(nuevoTag, idUsuarioQueCrea);
    }

    /*
     * NOTA: No hay endpoints @PutMapping o @DeleteMapping
     * porque tu base de datos (DB.sql) 
     * no tiene SPs para modificar o borrar tags (no hay 'mt' o 'bt').
     */
}