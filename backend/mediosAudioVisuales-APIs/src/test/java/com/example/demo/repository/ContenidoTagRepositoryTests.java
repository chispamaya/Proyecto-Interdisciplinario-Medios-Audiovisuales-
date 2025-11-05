package com.example.demo.repository;

import com.example.demo.dto.contenidoTag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ContenidoTagRepositoryTests {

    @Autowired
    private ContenidoTagRepository contenidoTagRepository;

    // IDs de prueba (Deben existir en la BD por el Paso 0)
    private static final Long ID_USUARIO_AUDITORIA = 8L; // Admin
    private static final Long ID_CONTENIDO_PRUEBA = 1L;
    private static final Long ID_TAG_PRUEBA = 1L;

    @Test
    void probarCrearYBorrarContenidoTag() {
        System.out.println("--- Probando ContenidoTagRepository ---");

        // 1. Probamos CREAR (SP 'cct')
        System.out.println("Probando SP 'cct' (crearContenidoTag)...");
        contenidoTag nuevoLink = new contenidoTag();
        nuevoLink.setIdContenido(ID_CONTENIDO_PRUEBA);
        nuevoLink.setIdTag(ID_TAG_PRUEBA);

        String msgCrear = contenidoTagRepository.crearContenidoTag(nuevoLink, ID_USUARIO_AUDITORIA);
        assertEquals("Tag añadido con éxito al contenido.", msgCrear);

        // 2. Probamos BORRAR (SP 'bct')
        // El SP 'bct' borra todos los tags de un contenido, no solo uno
        System.out.println("Probando SP 'bct' (borrarTagsPorContenido) para Contenido ID: " + ID_CONTENIDO_PRUEBA);
        
        String msgBorrar = contenidoTagRepository.borrarTagsPorContenido(ID_CONTENIDO_PRUEBA, ID_USUARIO_AUDITORIA);
        assertEquals("Contenido-Tag eliminado con éxito.", msgBorrar);

        System.out.println("--- PRUEBA DE CONTENIDO-TAG COMPLETADA ---");
    }
}