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
class EncuestaRepositoryTests {

    @Autowired
    private EncuestaRepository encuestaRepository;

    // Inyectamos OpcionERepository para CREAR las opciones de la prueba
    @Autowired
    private OpcionERepository opcionERepository;

    // Asumimos que el usuario con ID 1 (Admin) existe según tu DB.sql
    private static final Long ID_USUARIO_AUDITORIA = 1L;

    @Test
    void probarCrearYBuscarEncuestaConOpciones() {
        System.out.println("--- INICIANDO PRUEBA DE EncuestaRepository ---");

        // --- 1. Probar SP 'cen' (crearEncuesta) ---
        System.out.println("Probando SP 'cen' (crearEncuesta)...");
        Encuesta nuevaEncuesta = new Encuesta();
        String pregunta = "Pregunta de prueba " + System.currentTimeMillis() + "?";
        nuevaEncuesta.setPreguntar(pregunta);
        nuevaEncuesta.setIdUsuario(ID_USUARIO_AUDITORIA); // Creada por el usuario 1

        // Ejecutamos la creación
        Long nuevoId = encuestaRepository.crearEncuesta(nuevaEncuesta, ID_USUARIO_AUDITORIA);

        // Verificamos que nos devolvió un ID
        assertNotNull(nuevoId, "El SP 'cen' no devolvió un ID (idE es null)");
        assertTrue(nuevoId > 0, "El SP 'cen' devolvió un ID inválido");
        System.out.println("Respuesta del SP 'cen': Encuesta creada con ID: " + nuevoId);

        // --- 2. Setup: Añadir opciones (usando SP 'co') ---
        // Necesitamos esto para que el SP 's' (que tiene un JOIN) encuentre algo
        System.out.println("Añadiendo opciones de prueba (SP 'co')...");
        OpcionE op1 = new OpcionE();
        op1.setIdEncuesta(nuevoId);
        op1.setOpcion("Opción Test A");
        opcionERepository.crearOpcion(op1, 1, ID_USUARIO_AUDITORIA);
        
        OpcionE op2 = new OpcionE();
        op2.setIdEncuesta(nuevoId);
        op2.setOpcion("Opción Test B");
        opcionERepository.crearOpcion(op2, 2, ID_USUARIO_AUDITORIA);
        System.out.println("Opciones añadidas.");

        // --- 3. Probar SP 's' (buscarEncuestaConOpcionesYVotos) ---
        System.out.println("Probando SP 's' para buscar por ID (" + nuevoId + ")...");
        
        List<EncuestaResultado> resultados = encuestaRepository.buscarEncuestaConOpcionesYVotos(nuevoId);

        // Verificaciones
        assertNotNull(resultados, "La llamada a 's' devolvió null");
        // Debe devolver 2 filas, una por cada opción que creamos
        assertEquals(2, resultados.size(), "El SP 's' no devolvió las 2 filas esperadas"); 

        System.out.println("Prueba 's' con ID exitosa. Filas devueltas: " + resultados.size());

        // Verificamos el contenido de la primera fila (Opción A)
        EncuestaResultado resA = resultados.get(0);
        assertEquals(nuevoId, resA.getIdEncuesta());
        assertEquals(pregunta, resA.getPreguntar());
        assertEquals("Opción Test A", resA.getOpcion());
        assertEquals(0, resA.getTotalVotos(), "Los votos iniciales deben ser 0"); // Nadie votó todavía

        // Verificamos el contenido de la segunda fila (Opción B)
        EncuestaResultado resB = resultados.get(1);
        assertEquals(nuevoId, resB.getIdEncuesta());
        assertEquals("Opción Test B", resB.getOpcion());
        assertEquals(0, resB.getTotalVotos());
        
        System.out.println("Contenido verificado. " + resA.getOpcion() + " (Votos: " + resA.getTotalVotos() + ")");
    }
}