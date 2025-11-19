package com.example.demo.controller;

import com.example.demo.dto.Emision;
import com.example.demo.service.EmisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emisiones")
@CrossOrigin(origins = "http://localhost:5173")
public class EmisionController {

    @Autowired
    private EmisionService emisionService;

    // 1. Listar todas las emisiones (ControlDeEmision.jsx)
    @GetMapping
    public ResponseEntity<List<Emision>> listarEmisiones() {
        return ResponseEntity.ok(emisionService.listarEmisiones());
    }

    // 2. Poner una emisión EN VIVO
    @PutMapping("/{id}/vivo")
    public ResponseEntity<String> ponerEnVivo(@PathVariable Long id, @RequestParam Long idUsuarioAuditoria) {
        try {
            String mensaje = emisionService.ponerEnVivo(id, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 // En EmisionController.java
    @PostMapping("/comenzar")
    public Emision comenzarTransmision(@RequestBody Map<String, Long> payload) {
        Long idPrograma = payload.get("idPrograma");
        Long idUsuario = 1L; // TODO: Sacar del token de seguridad real
        
        return emisionService.comenzarEmision(idPrograma, idUsuario);
    }

    // --- Endpoint para apagar (Botón Rojo) ---
    @PutMapping("/{id}/finalizar")
    public String finalizarTransmision(@PathVariable Long id) {
        Long idUsuario = 1L; 
        return emisionService.finalizarEmision(id, idUsuario);
    }
    @PutMapping("/{id}/apagado")
    public ResponseEntity<String> sacarDeVivo(@PathVariable Long id, @RequestParam Long idUsuarioAuditoria) {
        try {
            String mensaje = emisionService.sacarDeVivo(id, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}