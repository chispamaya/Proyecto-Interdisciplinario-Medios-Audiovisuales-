package com.example.demo.controller;

import com.example.demo.dto.Emision;
import com.example.demo.service.EmisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emisiones") // Puse "emisiones" en plural
@CrossOrigin(origins = "*")
public class EmisionController {

    @Autowired
    private EmisionService emisionService; // Inyecta el servicio

    // GET (Traer todos)
    @GetMapping
    public List<Emision> getAllEmisiones() {
        return emisionService.findAll();
    }

    // GET (Traer uno por ID)
    @GetMapping("/{id}")
    public Emision getEmisionById(@PathVariable Long id) {
        return emisionService.findById(id).orElse(null);
    }

    // POST (Crear uno nuevo)
    @PostMapping
    public Emision createEmision(@RequestBody Emision emision) {
        return emisionService.save(emision);
    }

    // PUT (Actualizar uno existente)
    @PutMapping("/{id}")
    public Emision updateEmision(@PathVariable Long id, @RequestBody Emision emisionDetails) {
        emisionDetails.setId(id);
        return emisionService.save(emisionDetails);
    }

    // DELETE (Borrar uno)
    @DeleteMapping("/{id}")
    public void deleteEmision(@PathVariable Long id) {
        emisionService.deleteById(id);
    }
}