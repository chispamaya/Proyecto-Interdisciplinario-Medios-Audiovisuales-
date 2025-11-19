package com.example.demo.service;

import com.example.demo.dto.Tag;
import com.example.demo.dto.TagReporteDTO; // Importar DTO
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service 
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public String crearTag(Tag nuevoTag, Long idUsuarioQueCrea) {
        if (nuevoTag.getTag() == null || nuevoTag.getTag().isEmpty()) {
            return "Error: El tag no puede estar vacío.";
        }
        return tagRepository.crearTag(nuevoTag, idUsuarioQueCrea);
    }
  
    public List<Tag> listarTodosLosTags() {
        return tagRepository.listarTodosLosTags();
    }
    
    public Tag buscarTagPorId(Long id) {
        return tagRepository.buscarTagPorId(id);
    }

    // Método para resolver nombres a IDs (usado al crear contenido)
    @Transactional
    public List<Long> resolverIdsDeTags(List<String> nombresTags, Long idUsuarioAuditoria) {
        List<Long> idsFinales = new ArrayList<>();
        if (nombresTags == null || nombresTags.isEmpty()) return idsFinales;

        for (String nombreRaw : nombresTags) {
            String nombre = nombreRaw.trim(); 
            if (nombre.isEmpty()) continue;

            Long idExistente = tagRepository.buscarIdTagPorNombre(nombre);

            if (idExistente != null) {
                idsFinales.add(idExistente);
            } else {
                Tag nuevo = new Tag();
                nuevo.setTag(nombre);
                tagRepository.crearTag(nuevo, idUsuarioAuditoria);
                Long idNuevo = tagRepository.buscarIdTagPorNombre(nombre);
                if (idNuevo != null) {
                    idsFinales.add(idNuevo);
                }
            }
        }
        return idsFinales;
    }

    // --- 🚀 NUEVO MÉTODO SERVICIO ---
    public List<TagReporteDTO> obtenerReporteReacciones() {
        return tagRepository.obtenerReporteReaccionesPorTag();
    }
}