package com.example.demo.controller;

import com.example.demo.dto.Auditoria;
import com.example.demo.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "http://localhost:5173")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    // 1. Obtener el historial completo.
    @GetMapping
    public ResponseEntity<List<Auditoria>> listarTodo() {
        return ResponseEntity.ok(auditoriaService.listarAuditorias());
    }

    /**
     * 2. Filtrar por Tabla y Acción.
     * Endpoint: GET /api/auditoria/filtro?tabla=programas&accion=DELETE
     */
    @GetMapping("/filtro")
    public ResponseEntity<List<Auditoria>> filtrarSimple(
            @RequestParam String tabla, 
            @RequestParam String accion) {
        
        List<Auditoria> lista = auditoriaService.filtrarPorTablaYAccion(tabla, accion);
        return ResponseEntity.ok(lista);
    }

    /**
     * 3. Filtrar por Usuario, Tabla y Acción.
     * Endpoint: GET /api/auditoria/filtro/usuario?idUsuario=8&tabla=programas&accion=INSERT
     */
    @GetMapping("/filtro/usuario")
    public ResponseEntity<List<Auditoria>> filtrarAvanzado(
            @RequestParam Long idUsuario,
            @RequestParam String tabla,
            @RequestParam String accion) {
        
        List<Auditoria> lista = auditoriaService.filtrarPorUsuarioTablaYAccion(idUsuario, tabla, accion);
        return ResponseEntity.ok(lista);
    }
}