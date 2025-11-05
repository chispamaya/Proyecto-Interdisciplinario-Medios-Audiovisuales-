package com.example.demo.repository;

import com.example.demo.dto.Emision;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmisionRepositoryTests {

    @Autowired
    private EmisionRepository emisionRepository;

    // IDs de prueba (Deben existir en la BD por el Paso 0)
    private static final Long ID_USUARIO_AUDITORIA = 8L; // Admin
    private static final Long ID_EMISION_PRUEBA = 1L;    // Emision de prueba

    @Test
    void probarModificarEmision() {
        System.out.println("--- Probando EmisionRepository ---");

        // 1. Buscamos la emisión (ID 1) que insertamos en Paso 0
        System.out.println("Buscando emisión de prueba (ID 1)...");
        Emision emision = emisionRepository.buscarEmisionPorId(ID_EMISION_PRUEBA);
        assertNotNull(emision);
        // El Paso 0 la insertó como 'false' (no en vivo)
        assertFalse(emision.getEnVivo()); 

        // 2. Modificamos la emisión a 'true' (en vivo)
        emision.setEnVivo(true);
        System.out.println("Probando SP 'me' (modificarEmision) para poner enVivo=true...");
        
        String msgModificar = emisionRepository.modificarEmision(emision, ID_USUARIO_AUDITORIA);
        assertEquals("Datos de la emisión actualizados con éxito.", msgModificar);

        // 3. Verificamos el cambio
        Emision emisionModificada = emisionRepository.buscarEmisionPorId(ID_EMISION_PRUEBA);
        assertNotNull(emisionModificada);
        assertTrue(emisionModificada.getEnVivo()); // Debería ser 'true' ahora
        System.out.println("Verificación exitosa: enVivo es true.");

        // 4. Limpieza: Devolvemos la emisión a su estado original (false)
        emision.setEnVivo(false);
        emisionRepository.modificarEmision(emision, ID_USUARIO_AUDITORIA);
        System.out.println("Limpieza completada: enVivo devuelto a false.");

        System.out.println("--- PRUEBA DE EMISION COMPLETADA ---");
    }
}