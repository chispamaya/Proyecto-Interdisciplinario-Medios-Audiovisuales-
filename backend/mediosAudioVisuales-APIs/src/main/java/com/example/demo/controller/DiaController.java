package com.example.demo.controller;

import com.example.demo.dto.Dia;
import com.example.demo.service.DiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dias")
@CrossOrigin(origins = "http://localhost:5173")
public class DiaController {

    @Autowired
    private DiaService diaService;

    /**
     * Obtener los días asignados a un programa (Para ABMProgramasForm).
     */
    @GetMapping("/programa/{idPrograma}")
    public ResponseEntity<List<Dia>> listarDiasDePrograma(@PathVariable Long idPrograma) {
        return ResponseEntity.ok(diaService.listarDiasPorPrograma(idPrograma));
    }

    /**
     * Crear una asignación de día (Para ArmadoParrillaHoraria).
     */
    @PostMapping
    public ResponseEntity<String> crearAsignacion(@RequestBody Dia dia, @RequestParam Long idUsuarioAuditoria) {
        try {
            String mensaje = diaService.crearAsignacionDia(dia, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Borrar una asignación de día por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarAsignacion(@PathVariable Long id, @RequestParam Long idUsuarioAuditoria) {
        try {
            String mensaje = diaService.borrarAsignacionDia(id, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Actualizar masivamente los días de un programa (Para ABMProgramasForm).
     */
    @PutMapping("/programa/{idPrograma}")
    public ResponseEntity<String> actualizarDias(
            @PathVariable Long idPrograma,
            @RequestBody List<LocalDate> nuevasFechas,
            @RequestParam Long idUsuarioAuditoria) {
        try {
            diaService.actualizarDiasParaPrograma(idPrograma, nuevasFechas, idUsuarioAuditoria);
            return ResponseEntity.ok("Días del programa actualizados correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar días: " + e.getMessage());
        }
    }
    
    /**
     * Listar todos los días de la parrilla.
     */
    @GetMapping("/todos")
    public ResponseEntity<List<Dia>> listarTodos() {
        return ResponseEntity.ok(diaService.listarParrillaCompleta());
    }
}