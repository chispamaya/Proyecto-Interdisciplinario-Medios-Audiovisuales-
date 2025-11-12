package com.example.demo.service;

import com.example.demo.dto.Tag;
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}