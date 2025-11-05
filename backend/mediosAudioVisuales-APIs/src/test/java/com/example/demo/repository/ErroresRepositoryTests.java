package com.example.demo.repository;

import com.example.demo.dto.Errores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ErroresRepositoryTests {

    @Autowired
    private ErroresRepository erroresRepository;

    /**
     * Esta prueba solo verifica que el SP 's' para 'errores' funcione
     * y que el ErroresRowMapper (con la conversión de Timestamp a LocalDateTime)
     * no falle.
     */
    @Test
    void probarListarErrores() {
        System.out.println("--- INICIANDO PRUEBA DE ErroresRepository ---");

        // --- Probar SP 's' (listarTodosLosErrores) ---
        System.out.println("Probando SP 's' (listarTodosLosErrores)...");
        
        List<Errores> listaDeErrores = erroresRepository.listarTodosLosErrores();

        // La prueba principal es que la llamada no falle (devuelva null).
        // Si la lista está vacía (size 0), la prueba también es exitosa,
        // ya que significa que no hay errores en la BD, pero la consulta
        // y el mapeo funcionaron.
        assertNotNull(listaDeErrores, "La llamada a 's'('errores', null) devolvió null");
        
        System.out.println("Prueba 's' exitosa. Errores encontrados: " + listaDeErrores.size());

        // Si hay errores, podemos verificar que el mapeo de fecha funcionó
        if (!listaDeErrores.isEmpty()) {
            System.out.println("Último error registrado: " + listaDeErrores.get(0).getMensaje());
            // Verificamos que el campo LocalDateTime (fechaYHora) no sea nulo
            assertNotNull(listaDeErrores.get(0).getFechaYHora(), "El mapeo de fechaYHora (toLocalDateTime) falló y devolvió null");
        }
    }
}