package com.example.demo.controller;

import com.example.demo.dto.AudienciaCon;
import com.example.demo.service.AudienciaConService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audienciacon") // La URL base
@CrossOrigin(origins = "*") // Habilita CORS
public class AudienciaConController {

    @Autowired
    private AudienciaConService audienciaConService; // Inyecta el servicio

    // GET (Traer todos)
    @GetMapping
    public List<AudienciaCon> getAllAudienciaCon() {
        return audienciaConService.findAll();
    }

    // GET (Traer uno por ID)
    @GetMapping("/{id}")
    public AudienciaCon getAudienciaConById(@PathVariable Long id) {
        return audienciaConService.findById(id).orElse(null);
    }

    // POST (Crear uno nuevo)
    @PostMapping
    public AudienciaCon createAudienciaCon(@RequestBody AudienciaCon audienciaCon) {
        return audienciaConService.save(audienciaCon);
    }

    // PUT (Actualizar uno existente)
    @PutMapping("/{id}")
    public AudienciaCon updateAudienciaCon(@PathVariable Long id, @RequestBody AudienciaCon audienciaDetails) {
        audienciaDetails.setId(id); // Asumimos que el DTO tiene setId
        return audienciaConService.save(audienciaDetails);
    }

    // DELETE (Borrar uno)
    @DeleteMapping("/{id}")
    public void deleteAudienciaCon(@PathVariable Long id) {
        audienciaConService.deleteById(id);
    }
}