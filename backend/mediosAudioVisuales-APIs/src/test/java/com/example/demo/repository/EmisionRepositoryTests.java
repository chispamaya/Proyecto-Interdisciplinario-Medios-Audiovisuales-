package com.example.demo.repository;

import com.example.demo.dto.Emision;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prueba de INTEGRACIÓN para EmisionRepository.
 */
@SpringBootTest
public class EmisionRepositoryTests {

    @Autowired
    private EmisionRepository emisionRepository;

  
    private static final Long ID_USUARIO_AUDITORIA = 1L;
    private static final Long ID_PROGRAMA_PRUEBA = 1L;
    private static final Long ID_EMISION_PRUEBA = 1L; // Emisión a modificar

    @Test
    void testModificarEmision() {
        System.out.println("--- Probando EmisionRepository ---");

        // 1. Preparamos el DTO
        Emision emision = new Emision();
        emision.setId(ID_EMISION_PRUEBA); // El ID de la emisión que queremos cambiar
        emision.setEnVivo(true);          // El nuevo valor
        emision.setIdPrograma(ID_PROGRAMA_PRUEBA); // El programa asociado

        // 2. Llamamos al SP 'me'
        String msgModificar = emisionRepository.modificarEmision(emision, ID_USUARIO_AUDITORIA);
        
        // 3. Verificamos el mensaje de éxito del SP 'me'
        assertEquals("Datos de la emisión actualizados con éxito.", msgModificar);
        System.out.println("Prueba 'me' (Modificar Emision) exitosa.");
    }
}