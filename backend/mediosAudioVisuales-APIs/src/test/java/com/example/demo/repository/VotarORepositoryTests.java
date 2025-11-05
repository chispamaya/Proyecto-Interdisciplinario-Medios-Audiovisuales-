package com.example.demo.repository;

import com.example.demo.dto.Encuesta;
import com.example.demo.dto.EncuestaResultado;
import com.example.demo.dto.OpcionE;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VotarORepositoryTests {

    @Autowired
    private VotarORepository votarORepository; // El repo que queremos probar

    // --- Dependencias necesarias para el Setup ---
    @Autowired
    private EncuestaRepository encuestaRepository;
    
    @Autowired
    private OpcionERepository opcionERepository;

    // Asumimos que el usuario 1 (Admin) y el 12 (Espectador) existen en la BD
    private static final Long ID_USUARIO_AUDITORIA = 1L; 
    private static final Long ID_USUARIO_VOTANTE = 12L; 

    @Test
    void probarVotarOpcion() {
        System.out.println("--- INICIANDO PRUEBA DE VotarORepository ---");

        // --- 1. Setup: Crear Encuesta (SP 'cen') ---
        Encuesta nuevaEncuesta = new Encuesta();
        nuevaEncuesta.setPreguntar("¿Pregunta para Votar " + System.currentTimeMillis() + "?");
        nuevaEncuesta.setIdUsuario(ID_USUARIO_AUDITORIA);
        
        Long nuevaEncuestaId = encuestaRepository.crearEncuesta(nuevaEncuesta, ID_USUARIO_AUDITORIA);
        assertNotNull(nuevaEncuestaId, "Fallo el setup: No se pudo crear la encuesta");
        System.out.println("Setup: Encuesta creada con ID: " + nuevaEncuestaId);

        // --- 2. Setup: Crear Opción (SP 'co') ---
        OpcionE opcion1 = new OpcionE();
        opcion1.setIdEncuesta(nuevaEncuestaId);
        opcion1.setOpcion("Opción para Votar");
        
        String msgOpcion = opcionERepository.crearOpcion(opcion1, 1, ID_USUARIO_AUDITORIA);
        assertEquals("Opcion 1 creada con éxito.", msgOpcion, "Fallo el setup: El SP 'co' no respondió lo esperado");
        System.out.println("Setup: Opción creada.");

        // --- 3. Setup: Obtener el ID de la opción que acabamos de crear (SP 's') ---
        List<EncuestaResultado> resultados = encuestaRepository.buscarEncuestaConOpcionesYVotos(nuevaEncuestaId);
        assertNotNull(resultados, "Fallo el setup: No se pudo recuperar la opción creada");
        assertFalse(resultados.isEmpty(), "Fallo el setup: La lista de opciones está vacía");
        
        Long idOpcionVotada = resultados.get(0).getIdOpcion();
        System.out.println("Setup: ID de la opción a votar: " + idOpcionVotada);

        // --- 4. Prueba: Ejecutar el voto (SP 'vo') ---
        System.out.println("Probando SP 'vo' (votarOpcion)...");
        String msgVoto = votarORepository.votarOpcion(
            idOpcionVotada, 
            ID_USUARIO_VOTANTE, 
            nuevaEncuestaId, 
            ID_USUARIO_AUDITORIA
        );
        
        // Verificamos el mensaje de tu SP 'vo'
        assertEquals("Voto ingresado con éxito.", msgVoto, "El SP 'vo' no devolvió el mensaje esperado");
        System.out.println("Respuesta 'vo': " + msgVoto);

        // --- 5. Verificación Opcional: Verificamos que el voto se contó (SP 's') ---
        System.out.println("Verificando el conteo de votos (SP 's')...");
        List<EncuestaResultado> resultadosDespuesDeVotar = encuestaRepository.buscarEncuestaConOpcionesYVotos(nuevaEncuestaId);
        assertEquals(1, resultadosDespuesDeVotar.get(0).getTotalVotos(), "El voto no fue contado por el SP 's'");
        System.out.println("¡Éxito! El SP 's' ahora reporta " + resultadosDespuesDeVotar.get(0).getTotalVotos() + " voto(s).");
    }
}