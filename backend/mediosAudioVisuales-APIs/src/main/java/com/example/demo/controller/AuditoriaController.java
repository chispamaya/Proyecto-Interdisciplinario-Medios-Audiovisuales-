package com.example.demo.controller;

import com.example.demo.dto.Auditoria;
import com.example.demo.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "*")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService; // Inyecta el servicio

    // GET (Traer todos)
    @GetMapping
    public List<Auditoria> getAllAuditorias() {
        return auditoriaService.findAll();
    }

    // GET (Traer uno por ID)
    @GetMapping("/{id}")
    public Auditoria getAuditoriaById(@PathVariable Long id) {
        return auditoriaService.findById(id).orElse(null);
    }

    // POST (Crear uno nuevo)
    @PostMapping
    public Auditoria createAuditoria(@RequestBody Auditoria auditoria) {
        return auditoriaService.save(auditoria);
    }

    // PUT (Actualizar uno existente)
    @PutMapping("/{id}")
    public Auditoria updateAuditoria(@PathVariable Long id, @RequestBody Auditoria auditoriaDetails) {
        auditoriaDetails.setId(id);
        return auditoriaService.save(auditoriaDetails);
    }

    // DELETE (Borrar uno)
    @DeleteMapping("/{id}")
    public void deleteAuditoria(@PathVariable Long id) {
        auditoriaService.deleteById(id);
    }
}