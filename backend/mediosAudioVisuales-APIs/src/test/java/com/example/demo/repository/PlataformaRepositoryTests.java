package com.example.demo.repository;

import com.example.demo.dto.Plataforma;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlataformaRepositoryTests {

    @Autowired
    private PlataformaRepository plataformaRepository;

    private static final Long ID_USUARIO_AUDITORIA = 1L; 
    @Test
    void probarCUDPlataforma() {
        System.out.println("--- INICIANDO PRUEBA DE REPOSITORIO DE PLATAFORMA ---");

        System.out.println("Probando SP 'cpl' (crearPlataforma)...");
        
        String nombrePlataforma = "PlataformaTest-" + System.currentTimeMillis();
        Plataforma nuevaPlataforma = new Plataforma();
        nuevaPlataforma.setNombre(nombrePlataforma);
        nuevaPlataforma.setTipo("TestTipo");

        String mensajeCrear = plataformaRepository.crearPlataforma(nuevaPlataforma, ID_USUARIO_AUDITORIA);

        assertEquals("Plataforma ingresada con éxito.", mensajeCrear);

        System.out.println("Listando plataformas para encontrar la nueva...");
        
        List<Plataforma> lista = plataformaRepository.listarTodasLasPlataformas();
        Plataforma plataformaCreada = lista.stream()
                .filter(p -> p.getNombre().equals(nombrePlataforma))
                .findFirst()
                .orElse(null);
        assertNotNull(plataformaCreada, "La plataforma no se encontró en la lista después de crearla.");
        Long idNuevo = plataformaCreada.getId();
        System.out.println("Plataforma creada con ID: " + idNuevo);

        System.out.println("Probando SP 'mpl' (modificarPlataforma)...");
        
        plataformaCreada.setNombre("NombreModificado-" + System.currentTimeMillis());
        plataformaCreada.setTipo("TipoModificado");
        
        String mensajeModificar = plataformaRepository.modificarPlataforma(plataformaCreada, ID_USUARIO_AUDITORIA);
        assertEquals("Datos de la plataforma actualizados con éxito.", mensajeModificar);

        System.out.println("Probando SP 'bpl' (borrarPlataforma)...");
        
        String mensajeBorrar = plataformaRepository.borrarPlataforma(idNuevo, ID_USUARIO_AUDITORIA);
        assertEquals("Plataforma borrada con éxito.", mensajeBorrar);
        
        System.out.println("--- PRUEBA DE PLATAFORMA COMPLETADA CON ÉXITO ---");
    }
}