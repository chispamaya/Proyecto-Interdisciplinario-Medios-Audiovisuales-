package com.example.demo.repository;

import com.example.demo.dto.Segmento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SegmentoRepositoryTests {

    @Autowired
    private SegmentoRepository segmentoRepository;

    private static final Long ID_USUARIO_AUDITORIA = 1L; 
    private static final Long ID_PROGRAMA_PRUEBA = 1L;  

    @Test
    void probarCrearListarYBorrarSegmento() {
        System.out.println("--- INICIANDO PRUEBA DE REPOSITORIO DE SEGMENTO ---");

        
        System.out.println("Probando SP 'cs' (crearSegmento)...");
        
        Segmento nuevoSegmento = new Segmento();
        nuevoSegmento.setEstadoAprobacion("Test");
        nuevoSegmento.setDuracion(120.5f);
        nuevoSegmento.setTitulo("Segmento de Prueba " + System.currentTimeMillis());
        nuevoSegmento.setIdPrograma(ID_PROGRAMA_PRUEBA);

        
        String mensajeCrear = segmentoRepository.crearSegmento(nuevoSegmento, ID_USUARIO_AUDITORIA);

       
        assertEquals("Segmento ingresado con éxito.", mensajeCrear);

        
        System.out.println("Probando SP 's' (listarTodosLosSegmentos)...");
        
        List<Segmento> listaDeSegmentos = segmentoRepository.listarTodosLosSegmentos();

        
        assertNotNull(listaDeSegmentos);
        assertTrue(listaDeSegmentos.size() > 0);

        System.out.println("Prueba de listado exitosa. Segmentos encontrados: " + listaDeSegmentos.size());

        
        Long idParaBorrar = listaDeSegmentos.get(listaDeSegmentos.size() - 1).getId();
        System.out.println("Probando SP 'bs' (borrarSegmento) con ID: " + idParaBorrar);
        
        String mensajeBorrar = segmentoRepository.borrarSegmento(idParaBorrar, ID_USUARIO_AUDITORIA);
        
       
        assertEquals("Segmento borrado con éxito.", mensajeBorrar);
        
        System.out.println("--- PRUEBA DE SEGMENTO COMPLETADA CON ÉXITO ---");
    }
}