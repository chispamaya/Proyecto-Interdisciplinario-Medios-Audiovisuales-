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

    // GET /api/auditoria
    @GetMapping
    public ResponseEntity<List<Auditoria>> listarTodo() {
        // CORREGIDO: Usamos el método real del servicio
        return ResponseEntity.ok(auditoriaService.listarAuditorias());
    }
}