package com.example.demo.controller;

import com.example.demo.dto.Encuesta;
import com.example.demo.dto.EncuestaResultado;
import com.example.demo.dto.VotarO;
import com.example.demo.service.EncuestaService;
import com.example.demo.service.VotarOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encuestas")
public class EncuestaController {

    @Autowired
    private EncuestaService encuestaService;

    @Autowired
    private VotarOService votarOService;

    // --- 1. ENDPOINT DE CREACIÓN (POST) ---
    @PostMapping
    public ResponseEntity<String> crearEncuestaConOpciones(
            @RequestBody Encuesta encuesta, // <-- USAMOS EL DTO DIRECTAMENTE
            @RequestParam Long idUsuarioAuditoria) {
        try {
            // Extraemos la lista que viene dentro del objeto encuesta
            String mensaje = encuestaService.crearEncuestaConOpciones(
                encuesta, 
                encuesta.getOpciones(), // <-- Aquí pasamos la lista
                idUsuarioAuditoria
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear: " + e.getMessage());
        }
    }

    // --- 2. ENDPOINT DE RESULTADOS INDIVIDUAL (GET) ---
    @GetMapping("/{idEncuesta}/resultados")
    public ResponseEntity<List<EncuestaResultado>> obtenerResultados(@PathVariable Long idEncuesta) {
        List<EncuestaResultado> resultados = encuestaService.buscarEncuestaCompleta(idEncuesta);
        if (resultados != null && !resultados.isEmpty()) {
            return ResponseEntity.ok(resultados);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- 3. ENDPOINT DE ACCIÓN DE VOTACIÓN (POST) ---
    @PostMapping("/votar")
    public ResponseEntity<String> registrarVoto(
            @RequestBody VotarO voto,
            @RequestParam Long idUsuarioAuditoria) {
        
        String mensaje = votarOService.votarEnOpcion(
            voto.getIdOpcion(), 
            voto.getIdUsuario(),
            voto.getIdEncuesta(), 
            idUsuarioAuditoria
        );
        
        if (mensaje.startsWith("Error:")) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
        }
        return ResponseEntity.status(HttpStatus.OK).body(mensaje);
    }

    // --- 4. ENDPOINT DE REPORTE COMPLETO (GET) ---
    @GetMapping("/reporte-completo")
    public ResponseEntity<List<EncuestaResultado>> obtenerReporteCompleto() {
        List<EncuestaResultado> resultados = encuestaService.buscarEncuestaCompleta(null);
        return ResponseEntity.ok(resultados != null ? resultados : List.of());
    }
}