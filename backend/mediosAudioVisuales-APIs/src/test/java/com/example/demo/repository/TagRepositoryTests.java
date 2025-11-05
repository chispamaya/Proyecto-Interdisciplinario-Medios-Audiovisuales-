package com.example.demo.repository;

import com.example.demo.dto.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TagRepositoryTests {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void probarCrearYListarTags() {
        System.out.println("--- INICIANDO PRUEBA DE REPOSITORIO DE TAG ---");

        
        System.out.println("Probando SP 'ct' (crearTag)...");
        
        Tag nuevoTag = new Tag();
        
        String nombreTag = "TagPrueba-" + System.currentTimeMillis();
        nuevoTag.setTag(nombreTag);

        
        String mensajeRespuesta = tagRepository.crearTag(nuevoTag, 1L);

        
        System.out.println("Respuesta del SP 'ct': " + mensajeRespuesta);
        assertEquals("Tag " + nombreTag + " creado con éxito.", mensajeRespuesta);

        
        System.out.println("Probando SP 's' (listarTodosLosTags)...");
        
        List<Tag> listaDeTags = tagRepository.listarTodosLosTags();

        
        assertNotNull(listaDeTags);
        assertTrue(listaDeTags.size() > 0);

        System.out.println("Prueba exitosa. Tags encontrados: " + listaDeTags.size());
    }
}