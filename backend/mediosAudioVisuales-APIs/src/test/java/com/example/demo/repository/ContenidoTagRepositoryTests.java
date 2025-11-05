package com.example.demo.repository;

import com.example.demo.dto.contenidoTag; // Ojo con el nombre de tu DTO
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prueba de INTEGRACIÓN para ContenidoTagRepository.
 */
@SpringBootTest
public class ContenidoTagRepositoryTests {

    @Autowired
    private ContenidoTagRepository contenidoTagRepository;

   
    private static final Long ID_USUARIO_AUDITORIA = 1L;
    private static final Long ID_CONTENIDO_PRUEBA = 1L;
    private static final Long ID_TAG_PRUEBA = 1L;

    @Test
    void testAsignarYBorrarTag() {
        System.out.println("--- Probando ContenidoTagRepository ---");

        // 1. Asignamos un Tag (SP 'cct')
        contenidoTag ct = new contenidoTag();
        ct.setIdContenido(ID_CONTENIDO_PRUEBA);
        ct.setIdTag(ID_TAG_PRUEBA);

        String msgCrear = contenidoTagRepository.crearContenidoTag(ct, ID_USUARIO_AUDITORIA);
        // Verificamos el mensaje de éxito del SP 'cct'
        assertEquals("Tag añadido con éxito al contenido.", msgCrear);
        System.out.println("Prueba 'cct' (Crear ContenidoTag) exitosa.");

        // 2. Borramos el Tag (SP 'bct')
        // (Esto borra TODOS los tags del contenido 1)
        String msgBorrar = contenidoTagRepository.borrarTagsPorContenido(ID_CONTENIDO_PRUEBA, ID_USUARIO_AUDITORIA);
        // Verificamos el mensaje de éxito del SP 'bct'
        assertEquals("Contenido-Tag eliminado con éxito.", msgBorrar);
        System.out.println("Prueba 'bct' (Borrar ContenidoTag) exitosa.");
    }
}