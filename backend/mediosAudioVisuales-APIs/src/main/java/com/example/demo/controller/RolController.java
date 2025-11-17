package com.example.demo.controller;

import com.example.demo.dto.PermisosRol;
import com.example.demo.dto.Rol;
import com.example.demo.service.PermisosRolService;
import com.example.demo.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el recurso 'Rol'.
 * Expone los métodos de LECTURA (GET) para los roles y
 * para las asignaciones de permisos a roles.
 * No se permite crear, modificar o borrar (POST, PUT, DELETE).
 */
@RestController
@RequestMapping("/api/roles") // URL Base: /api/roles
public class RolController {

    @Autowired
    private RolService rolService;

    @Autowired
    private PermisosRolService permisosRolService;

    /**
     * Endpoint para OBTENER TODOS los roles.
     * URL: HTTP GET /api/roles
     */
    @GetMapping
    public ResponseEntity<List<Rol>> listarTodosLosRoles() {
        
        // CORREGIDO: Llama al método que sí existe en tu RolService
        List<Rol> roles = rolService.listarTodosLosRoles();
        
        return ResponseEntity.ok(roles);
    }

    /**
     * Endpoint para OBTENER UN rol por su ID.
     * URL: HTTP GET /api/roles/8
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rol> buscarRolPorId(@PathVariable Long id) {
        
        // CORREGIDO: Llama al método que sí existe en tu RolService
        Rol rol = rolService.buscarRolPorId(id);
        
        if (rol != null) {
            return ResponseEntity.ok(rol);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para OBTENER TODAS las asignaciones (la tabla permisos_rol).
     * URL: HTTP GET /api/roles/asignaciones
     */
    @GetMapping("/asignaciones")
    public ResponseEntity<List<PermisosRol>> listarTodasLasAsignaciones() {
        
        // Llama al método que existe en tu PermisosRolService
        List<PermisosRol> asignaciones = permisosRolService.listarTodasLasAsignaciones();
        
        return ResponseEntity.ok(asignaciones);
    }
}