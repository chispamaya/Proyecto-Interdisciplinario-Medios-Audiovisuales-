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

    // 1. Crear asignación de día (ArmadoParrillaHoraria.jsx)
    @PostMapping
    public ResponseEntity<String> crearAsignacion(@RequestBody Dia dia, @RequestParam Long idUsuarioAuditoria) {
        try {
            // Incluye la validación de horarios dentro del servicio
            String mensaje = diaService.crearAsignacionDia(dia, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Borrar asignación de día (ArmadoParrillaHoraria.jsx)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarAsignacion(@PathVariable Long id, @RequestParam Long idUsuarioAuditoria) {
        try {
            String mensaje = diaService.borrarAsignacionDia(id, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Listar parrilla completa (ArmadoParrillaHoraria.jsx)
    @GetMapping("/todos")
    public ResponseEntity<List<Dia>> listarTodos() {
        return ResponseEntity.ok(diaService.listarParrillaCompleta());
    }
    
    // 4. Ver días de un programa (ABMProgramasForm.jsx - Llenar form)
    @GetMapping("/programa/{idPrograma}")
    public ResponseEntity<List<Dia>> listarDiasDePrograma(@PathVariable Long idPrograma) {
        return ResponseEntity.ok(diaService.listarDiasPorPrograma(idPrograma));
    }

    // 5. Actualizar días de un programa masivamente (ABMProgramasForm.jsx - Guardar)
    @PutMapping("/programa/{idPrograma}")
    public ResponseEntity<String> actualizarDias(
            @PathVariable Long idPrograma,
            @RequestBody List<LocalDate> nuevasFechas,
            @RequestParam Long idUsuarioAuditoria) {
        try {
            // Usa la lógica transaccional "modo lento"
            diaService.actualizarDiasParaPrograma(idPrograma, nuevasFechas, idUsuarioAuditoria);
            return ResponseEntity.ok("Días del programa actualizados correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar días: " + e.getMessage());
        }
    }
}