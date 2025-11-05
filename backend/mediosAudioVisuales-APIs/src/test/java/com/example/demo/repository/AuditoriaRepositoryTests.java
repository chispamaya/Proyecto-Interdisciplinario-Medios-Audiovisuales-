package com.example.demo.repository;

import com.example.demo.dto.Auditoria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Prueba de INTEGRACIÓN para AuditoriaRepository.
 */
@SpringBootTest
public class AuditoriaRepositoryTests {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Test
    void testObtenerTodasLasAuditorias() {
        System.out.println("--- Probando AuditoriaRepository ---");

        // 1. Llamamos al SP 's' para leer la tabla
        List<Auditoria> auditorias = auditoriaRepository.obtenerTodasLasAuditorias();

        // 2. Verificamos que la lista no sea nula (que el RowMapper funcionó)
        assertNotNull(auditorias, "La lista de auditorías no puede ser nula.");
        
        // Esta prueba es simple: solo verifica que la tabla (llena por triggers) se puede leer.
        System.out.println("Prueba 's' (Leer Auditoria) exitosa. Registros encontrados: " + auditorias.size());
    }
}