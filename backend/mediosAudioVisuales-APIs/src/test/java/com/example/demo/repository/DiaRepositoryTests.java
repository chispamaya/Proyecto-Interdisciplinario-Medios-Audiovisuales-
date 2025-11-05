package com.example.demo.repository;

import com.example.demo.dto.Dia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prueba de INTEGRACIÓN para DiaRepository.
 */
@SpringBootTest
public class DiaRepositoryTests {

    @Autowired
    private DiaRepository diaRepository;


    private static final Long ID_USUARIO_AUDITORIA = 1L;
    private static final Long ID_PROGRAMA_PRUEBA = 1L;

    @Test
    void testCrearDia() {
        System.out.println("--- Probando DiaRepository ---");

        // 1. Preparamos el DTO
        Dia dia = new Dia();
        dia.setDia(LocalDate.now()); // Asigna la fecha de hoy
        dia.setIdPrograma(ID_PROGRAMA_PRUEBA);

        // 2. Llamamos al SP 'cd'
        String msgCrear = diaRepository.crearDia(dia, ID_USUARIO_AUDITORIA);

        // 3. Verificamos el mensaje de éxito del SP 'cd'
        assertEquals("Dia asignado al programa con éxito.", msgCrear);
        System.out.println("Prueba 'cd' (Crear Dia) exitosa.");

        // NOTA: No probamos 'bd' (borrar) porque 'cd' no nos devuelve
        // el ID del día que acaba de crear.
    }
}