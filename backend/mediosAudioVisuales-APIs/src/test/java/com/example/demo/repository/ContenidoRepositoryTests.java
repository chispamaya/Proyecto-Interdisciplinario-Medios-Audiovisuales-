package com.example.demo.repository;

import com.example.demo.dto.Contenido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prueba de INTEGRACIÓN para ContenidoRepository.
 */
@SpringBootTest
public class ContenidoRepositoryTests {

    @Autowired
    private ContenidoRepository contenidoRepository;

    // --- IDs de prueba ---
    private static final Long ID_USUARIO_AUDITORIA = 1L; // Usuario que ejecuta
    private static final Long ID_USUARIO_PRUEBA = 1L;    // Dueño del contenido (asumir que existe)

    @Test
    void testCrearContenido() {
        System.out.println("--- Probando ContenidoRepository ---");

        // 1. Preparamos el DTO
        Contenido c = new Contenido();
        c.setFormato("mp4_test_junit");
        c.setRutaArchivo("/junit/test.mp4");
        c.setIdUsuario(ID_USUARIO_PRUEBA);

        // 2. Llamamos al SP 'cc'
        String msgCrear = contenidoRepository.crearContenido(c, ID_USUARIO_AUDITORIA);
        
        // 3. Verificamos el mensaje de éxito del SP 'cc'
        assertEquals("Contenido subido con éxito.", msgCrear);
        System.out.println("Prueba 'cc' (Crear Contenido) exitosa.");

        // NOTA: No probamos 'bc' (borrar) aquí porque 'cc' no nos devuelve
        // el ID del contenido que acaba de crear. 
    }
}