package com.example.demo.controller;

import com.example.demo.dto.Dia;
import com.example.demo.service.DiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dias") // Puse "dias" en plural
@CrossOrigin(origins = "*")
public class DiaController {

    @Autowired
    private DiaService diaService; // Inyecta el servicio

    // GET (Traer todos)
    @GetMapping
    public List<Dia> getAllDias() {
        return diaService.findAll();
    }

    // GET (Traer uno por ID)
    @GetMapping("/{id}")
    public Dia getDiaById(@PathVariable Long id) {
        return diaService.findById(id).orElse(null);
    }

    // POST (Crear uno nuevo)
    @PostMapping
    public Dia createDia(@RequestBody Dia dia) {
        return diaService.save(dia);
    }

    // PUT (Actualizar uno existente)
    @PutMapping("/{id}")
    public Dia updateDia(@PathVariable Long id, @RequestBody Dia diaDetails) {
        diaDetails.setId(id);
        return diaService.save(diaDetails);
    }

    // DELETE (Borrar uno)
    @DeleteMapping("/{id}")
    public void deleteDia(@PathVariable Long id) {
        diaService.deleteById(id);
    }
}