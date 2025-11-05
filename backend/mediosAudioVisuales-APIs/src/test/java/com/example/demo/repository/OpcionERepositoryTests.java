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
class OpcionERepositoryTests {

    @Autowired
    private OpcionERepository opcionERepository;

    // Necesitamos crear una encuesta primero, así que también inyectamos su repo
    @Autowired
    private EncuestaRepository encuestaRepository;

    // Asumimos que el usuario 1 (Admin) y el 12 (Espectador) existen
    private static final Long ID_USUARIO_AUDITORIA = 1L; 

    @Test
    void probarCrearOpcionYVotar() {
        System.out.println("--- INICIANDO PRUEBA DE OpcionERepository ---");

        // --- 1. Setup: Crear una Encuesta de prueba (usando SP 'cen') ---
        Encuesta nuevaEncuesta = new Encuesta();
        nuevaEncuesta.setPreguntar("¿Pregunta para Votar " + System.currentTimeMillis() + "?");
        nuevaEncuesta.setIdUsuario(ID_USUARIO_AUDITORIA);
        
        Long nuevaEncuestaId = encuestaRepository.crearEncuesta(nuevaEncuesta, ID_USUARIO_AUDITORIA);
        assertNotNull(nuevaEncuestaId, "Fallo el setup: No se pudo crear la encuesta");
        System.out.println("Setup: Encuesta creada con ID: " + nuevaEncuestaId);

        // --- 2. Probar SP 'co' (crearOpcion) ---
        System.out.println("Probando SP 'co' (crearOpcion)...");
        OpcionE opcion1 = new OpcionE();
        opcion1.setIdEncuesta(nuevaEncuestaId);
        opcion1.setOpcion("Opción para Votar");

        // Ejecutamos
        String msgOpcion = opcionERepository.crearOpcion(opcion1, 1, ID_USUARIO_AUDITORIA);
        
        // Verificamos el mensaje de tu SP 'co'
        assertEquals("Opcion 1 creada con éxito.", msgOpcion, "El SP 'co' no devolvió el mensaje esperado");
        System.out.println("Respuesta 'co': " + msgOpcion);

        // --- 3. Setup: Obtener el ID de la opción que acabamos de crear ---
        // (Usamos el método que probamos en el otro test)
        List<EncuestaResultado> resultados = encuestaRepository.buscarEncuestaConOpcionesYVotos(nuevaEncuestaId);
        assertNotNull(resultados, "No se pudo recuperar la opción creada");
        assertFalse(resultados.isEmpty(), "La lista de opciones está vacía");
        
        Long idOpcionVotada = resultados.get(0).getIdOpcion();
        System.out.println("Setup: ID de la opción a votar: " + idOpcionVotada);


    }
}