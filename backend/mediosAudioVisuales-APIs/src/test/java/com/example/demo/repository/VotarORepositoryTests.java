package com.example.demo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VotarORepositoryTests {

    @Autowired
    private VotarORepository votarORepository;

    // --- IDs DE PRUEBA BASADOS EN TU BASE DE DATOS REAL ---

    // Asumimos que el usuario 1 (admin@test.com) existe
    private final long ID_USUARIO_QUE_VOTA = 1L; 
    
    // El ID de la encuesta que me mostraste
    private final long ID_ENCUESTA = 1L;
    
    // El ID de "Opción A: Netflix"
    private final long ID_OPCION_A_VOTAR = 1L;
    
    // El ID de "Opción B: Disney+"
    private final long ID_OTRA_OPCION = 2L;
    
    // Usuario para auditoría (el mismo admin)
    private final long ID_USUARIO_AUDITORIA = 1L;

    @Test
    void probarVotarOpcion() {
        System.out.println("--- Probando votarOpcion con los IDs correctos ---");

        // --- PASO 1: Votar por la Opción 3 (Netflix) ---
        // Se llama con: idO=3, idU=1, idE=2, idUs=1
        String mensajeVoto = votarORepository.votarOpcion(
                ID_OPCION_A_VOTAR,    // idO = 1
                ID_USUARIO_QUE_VOTA,  // idU (quien vota) = 1
                ID_ENCUESTA,          // idE = 1
                ID_USUARIO_AUDITORIA  // idUs (logger) = 1
        );

        // 1. Ahora SÍ debería decir "Voto ingresado con éxito."
        assertEquals("Voto ingresado con éxito.", mensajeVoto);
        System.out.println("Prueba 'votarOpcion' exitosa.");

        // --- PRUEBAS ADICIONALES (Para probar la lógica de tu SP 'vo') ---

        // 2. Si volvemos a votar por la Opción 3, debería QUITAR el voto.
        String mensajeQuitarVoto = votarORepository.votarOpcion(
                ID_OPCION_A_VOTAR,
                ID_USUARIO_QUE_VOTA,
                ID_ENCUESTA,
                ID_USUARIO_AUDITORIA
        );
        assertEquals("Voto eliminado con éxito.", mensajeQuitarVoto, "El SP 'vo' no eliminó el voto al segundo clic.");

        // 3. Votamos por la Opción 3 de nuevo (para dejarlo como estaba)
        votarORepository.votarOpcion(ID_OPCION_A_VOTAR, ID_USUARIO_QUE_VOTA, ID_ENCUESTA, ID_USUARIO_AUDITORIA);
        
        // 4. Ahora votamos por OTRA opción (Opción 4 - Disney+)
        String mensajeCambiarVoto = votarORepository.votarOpcion(
                ID_OTRA_OPCION, // idO = 4
                ID_USUARIO_QUE_VOTA,
                ID_ENCUESTA,
                ID_USUARIO_AUDITORIA
        );
        assertEquals("Voto actualizado con éxito.", mensajeCambiarVoto, "El SP 'vo' no actualizó el voto a una nueva opción.");
    }
}