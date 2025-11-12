package com.example.demo.controller;

import com.example.demo.dto.Permisos;
import com.example.demo.service.PermisosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el recurso 'Permisos'.
 * Este controlador solo expone métodos de LECTURA (GET) para el
 * catálogo de permisos, ya que el servicio no implementa ABM.
 */
@RestController
@RequestMapping("/api/permisos")
public class PermisosController {

    @Autowired
    private PermisosService permisosService;

    /**
     * Endpoint 1: OBTENER TODOS los permisos.
     * URL: HTTP GET /api/permisos
     * Devuelve: List<Permisos> (Objetos con ID y tipoPermiso)
     */
    @GetMapping
    public ResponseEntity<List<Permisos>> listarTodos() {
        // Llama al método que existe en tu servicio
        List<Permisos> listaPermisos = permisosService.listarTodosLosPermisos();
        return ResponseEntity.ok(listaPermisos);
    }

    /**
     * Endpoint 2: OBTENER los permisos (como Strings) asignados a un ROL.
     * URL: HTTP GET /api/permisos/por-rol?rol={nombreDelRol}
     * Devuelve: List<String> (Solo los nombres de permisos)
     */
    @GetMapping("/por-rol")
    public ResponseEntity<List<String>> buscarPermisosPorRol(
            @RequestParam String rol) {
        
        // Llama al método que existe en tu servicio
        List<String> permisosDelRol = permisosService.buscarPermisosPorRol(rol);
        return ResponseEntity.ok(permisosDelRol);
    }
}