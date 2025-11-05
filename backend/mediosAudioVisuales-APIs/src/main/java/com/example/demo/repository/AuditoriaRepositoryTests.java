package com.example.demo.repository;

import com.example.demo.dto.Auditoria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuditoriaRepositoryTests {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Test
    void probarListarAuditorias() {
        System.out.println("--- Probando AuditoriaRepository (SP 's') ---");

        // Simplemente llamamos al método de listar
        List<Auditoria> listaDeAuditorias = auditoriaRepository.obtenerTodasLasAuditorias();

        // Verificamos que la lista no sea nula.
        // (Puede estar vacía si no se han ejecutado otras pruebas que disparen los triggers)
        assertNotNull(listaDeAuditorias);

        System.out.println("Prueba de listado de auditoría exitosa. Registros encontrados: " + listaDeAuditorias.size());
    }
}