package com.example.demo.controller;

import com.example.demo.dto.Tag;
import com.example.demo.dto.TagReporteDTO;
import com.example.demo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public List<Tag> listarTodosLosTags() {
        return tagService.listarTodosLosTags();
    }

    @GetMapping("/{id}")
    public Tag buscarTagPorId(@PathVariable Long id) {
        return tagService.buscarTagPorId(id);
    }

    @PostMapping
    public String crearTag(@RequestBody Tag nuevoTag) {
        Long idUsuarioQueCrea = 1L; 
        return tagService.crearTag(nuevoTag, idUsuarioQueCrea);
    }
    
    // --- ENDPOINT REPORTE ---
    @GetMapping("/reporte")
    public List<TagReporteDTO> obtenerReporteDeTags() {
        return tagService.obtenerReporteReacciones();
    }
}