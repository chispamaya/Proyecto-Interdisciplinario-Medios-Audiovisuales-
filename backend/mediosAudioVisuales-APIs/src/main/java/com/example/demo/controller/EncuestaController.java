package com.example.demo.controller;

import com.example.demo.dto.Encuesta;
import com.example.demo.dto.EncuestaResultado;
import com.example.demo.dto.OpcionE;
import com.example.demo.dto.VotarO;
import com.example.demo.service.EncuestaService;
import com.example.demo.service.VotarOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// ¡Ya no se importa @Transactional!
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el recurso 'Encuesta'.
 * Agrupa la lógica de Encuesta, Opciones y Votos.
 */
@RestController
@RequestMapping("/api/encuestas")
public class EncuestaController {

    @Autowired
    private EncuestaService encuestaService;

    @Autowired
    private VotarOService votarOService;

    // --- 1. ENDPOINT DE CREACIÓN (POST) ---

    @PostMapping
    // (CAMBIO: Se quitó @Transactional de aquí)
    public ResponseEntity<String> crearEncuestaConOpciones(
            @RequestBody CrearEncuestaRequest requestBody, 
            @RequestParam Long idUsuarioAuditoria) {
        try {
            
            // (CAMBIO: Ahora llamamos al método unificado del EncuestaService)
            String mensaje = encuestaService.crearEncuestaConOpciones(
                requestBody.getEncuesta(), 
                requestBody.getOpciones(), 
                idUsuarioAuditoria
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);

        } catch (IllegalArgumentException e) {
            // Captura errores de validación de negocio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Captura errores de SQL o de la transacción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la encuesta: " + e.getMessage());
        }
    }

    // --- 2. ENDPOINT DE RESULTADOS (GET) ---
    // (Este método no tuvo cambios)
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
    // (Este método no tuvo cambios. Funciona gracias a la corrección del DTO VotarO)
    @PostMapping("/votar")
    public ResponseEntity<String> registrarVoto(
            @RequestBody VotarO voto,
            @RequestParam Long idUsuarioAuditoria) {
        
        String mensaje = votarOService.votarEnOpcion(
            voto.getIdOpcion(), 
            voto.getIdUsuario(),
            voto.getIdEncuesta(), // Esto ahora funciona
            idUsuarioAuditoria
        );
        
        if (mensaje.startsWith("Error:")) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
        }
        return ResponseEntity.status(HttpStatus.OK).body(mensaje);
    }
    
    // --- DTO INTERNO PARA EL REQUEST DE CREACIÓN ---
    // (Esta clase interna no tuvo cambios)
    static class CrearEncuestaRequest {
        private Encuesta encuesta;
        private List<OpcionE> opciones;

        // Getters y Setters
        public Encuesta getEncuesta() { return encuesta; }
        public void setEncuesta(Encuesta encuesta) { this.encuesta = encuesta; }
        public List<OpcionE> getOpciones() { return opciones; }
        public void setOpciones(List<OpcionE> opciones) { this.opciones = opciones; }
    }
}