package com.example.demo.controller;

import com.example.demo.dto.AudienciaCon;
import com.example.demo.dto.ReporteAudienciaDTO;
import com.example.demo.service.AudienciaConService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audiencia")
@CrossOrigin(origins = "http://localhost:5173")
public class AudienciaConController {

    @Autowired
    private AudienciaConService audienciaConService;

    /**
     * Endpoint para dar Like o Dislike.
     */
    @PostMapping("/voto")
    public ResponseEntity<String> votar(@RequestBody AudienciaCon voto, @RequestParam Long idUsuarioAuditoria) {
        try {
            String mensaje = audienciaConService.crearOModificarVoto(voto, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al votar: " + e.getMessage());
        }
    }

    /**
     * Endpoint para quitar un voto existente.
     */
    @DeleteMapping("/voto")
    public ResponseEntity<String> borrarVoto(@RequestParam Long idContenido, @RequestParam Long idUsuario, @RequestParam Long idUsuarioAuditoria) {
        try {
            String mensaje = audienciaConService.borrarVoto(idContenido, idUsuario, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para obtener el reporte de conteo de Likes/Dislikes.
     */
    @GetMapping("/reporte")
    public ResponseEntity<List<ReporteAudienciaDTO>> obtenerReporte() {
        return ResponseEntity.ok(audienciaConService.obtenerReporteAudiencia());
    }
}