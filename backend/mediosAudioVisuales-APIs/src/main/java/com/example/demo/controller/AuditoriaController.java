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

    // Traer todo
    @GetMapping
    public ResponseEntity<List<Auditoria>> listarTodo() {
        return ResponseEntity.ok(auditoriaService.listarAuditorias());
    }

    // Si agregaste los métodos de filtro al Service, descomenta esto:
    /*
    @GetMapping("/filtro")
    public ResponseEntity<List<Auditoria>> filtrar(@RequestParam String tabla, @RequestParam String accion) {
        return ResponseEntity.ok(auditoriaService.filtrarPorTablaYAccion(tabla, accion));
    }
    */
}