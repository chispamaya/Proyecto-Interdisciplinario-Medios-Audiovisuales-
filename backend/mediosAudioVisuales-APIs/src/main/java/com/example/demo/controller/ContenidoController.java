package com.example.demo.controller;

import com.example.demo.dto.Contenido;
import com.example.demo.dto.contenidoTag; // Ojo con la minúscula
import com.example.demo.service.ContenidoService;
import com.example.demo.service.ContenidoTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenido")
@CrossOrigin(origins = "*")
public class ContenidoController {

    @Autowired
    private ContenidoService contenidoService; // Servicio principal

    @Autowired
    private ContenidoTagService contenidoTagService; // Servicio secundario para los tags

    // --- CRUD BÁSICO PARA CONTENIDO ---

    // GET (Traer todos)
    @GetMapping
    public List<Contenido> getAllContenidos() {
        return contenidoService.findAll();
    }

    // GET (Traer uno por ID)
    @GetMapping("/{id}")
    public Contenido getContenidoById(@PathVariable Long id) {
        return contenidoService.findById(id).orElse(null);
    }

    // POST (Crear uno nuevo)
    @PostMapping
    public Contenido createContenido(@RequestBody Contenido contenido) {
        return contenidoService.save(contenido);
    }

    // PUT (Actualizar uno existente)
    @PutMapping("/{id}")
    public Contenido updateContenido(@PathVariable Long id, @RequestBody Contenido contenidoDetails) {
        contenidoDetails.setId(id);
        return contenidoService.save(contenidoDetails);
    }

    // DELETE (Borrar uno)
    @DeleteMapping("/{id}")
    public void deleteContenido(@PathVariable Long id) {
        contenidoService.deleteById(id);
    }

    // --- MÉTODO EXTRA PARA MANEJAR LOS TAGS (como dijiste) ---
    
    // POST (Asignar un Tag a un Contenido)
    @PostMapping("/{idContenido}/tag")
    public contenidoTag addTagToContenido(@PathVariable Long idContenido, @RequestBody contenidoTag tagRequest) {
        // Acá la lógica sería:
        // 1. Buscar el Contenido por idContenido
        // 2. Buscar (o crear) el Tag
        // 3. Crear la relación contenidoTag
        // Por ahora, solo guardamos la relación:
        
        // (Esto es un ejemplo, la lógica real iría en el Service)
        Contenido c = contenidoService.findById(idContenido).orElse(null);
        if (c != null) {
            // Asumimos que tagRequest tiene el ID del Tag
            tagRequest.setContenido(c); 
            return contenidoTagService.save(tagRequest);
        }
        return null;
    }
}