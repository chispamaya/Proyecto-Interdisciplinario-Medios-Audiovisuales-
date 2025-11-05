package com.example.demo.repository;

import com.example.demo.dto.Programa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProgramaRepositoryTests {

    @Autowired
    private ProgramaRepository programaRepository;

    private static final Long ID_USUARIO_AUDITORIA = 1L; 
    private static final Long ID_PLATAFORMA_PRUEBA = 1L;  
    
    @Test
    void probarCrearListarYBorrarPrograma() {
        System.out.println("--- INICIANDO PRUEBA DE REPOSITORIO DE PROGRAMA ---");

        System.out.println("Probando SP 'cpr' (crearPrograma)...");
        
        Programa nuevoPrograma = new Programa();
        nuevoPrograma.setCategoria("Test");
        nuevoPrograma.setNombre("Programa de Prueba " + System.currentTimeMillis());
        nuevoPrograma.setHoraInicio(LocalTime.of(10, 30)); 
        nuevoPrograma.setHoraFin(LocalTime.of(11, 30));  
        nuevoPrograma.setIdPlataforma(ID_PLATAFORMA_PRUEBA);
        nuevoPrograma.setFormatoArchivo("mp4");
        nuevoPrograma.setRutaArchivo("/ruta/test.mp4");

        String mensajeCrear = programaRepository.crearPrograma(nuevoPrograma, ID_USUARIO_AUDITORIA);

        assertEquals("Programa ingresado con éxito.", mensajeCrear);

        System.out.println("Probando 'listarTodosLosProgramas'...");
        
        List<Programa> listaDeProgramas = programaRepository.listarTodosLosProgramas();

        assertNotNull(listaDeProgramas);
        assertTrue(listaDeProgramas.size() > 0);

        System.out.println("Prueba de listado exitosa. Programas encontrados: " + listaDeProgramas.size());


        Long idParaBorrar = listaDeProgramas.get(listaDeProgramas.size() - 1).getId();
        System.out.println("Probando SP 'bpr' (borrarPrograma) con ID: " + idParaBorrar);
        
        String mensajeBorrar = programaRepository.borrarPrograma(idParaBorrar, ID_USUARIO_AUDITORIA);
        
        assertEquals("Programa borrado con éxito.", mensajeBorrar);
        
        System.out.println("--- PRUEBA DE PROGRAMA COMPLETADA CON ÉXITO ---");
    }
}