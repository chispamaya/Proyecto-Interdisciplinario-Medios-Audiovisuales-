package com.example.demo.controller;

import com.example.demo.dto.Encuesta;
import com.example.demo.dto.EncuestaResultado;
import com.example.demo.dto.OpcionE;
import com.example.demo.dto.VotarO;
import com.example.demo.service.EncuestaService;
import com.example.demo.service.OpcionEService;
import com.example.demo.service.VotarOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // Necesario para crear la Encuesta
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el recurso 'Encuesta'.
 * Agrupa la lógica de Encuesta (EncuestaService), Opciones (OpcionEService)
 * y Votos (VotarOService) en el endpoint /api/encuestas.
 */
@RestController
@RequestMapping("/api/encuestas")
public class EncuestaController {

    // (1) Inyección de los tres servicios.
    @Autowired
    private EncuestaService encuestaService;

    @Autowired
    private OpcionEService opcionEService;

    @Autowired
    private VotarOService votarOService;

    // --- 1. ENDPOINT DE CREACIÓN (POST) ---

    /**
     * Endpoint para CREAR una nueva encuesta con sus opciones.
     * URL: HTTP POST /api/encuestas
     *
     * @param requestBody Un JSON que contiene la Encuesta y una lista de OpcionesE.
     * @param idUsuarioAuditoria ID del usuario que crea la encuesta.
     * @return Mensaje de éxito o error.
     *
     * NOTA IMPORTANTE: Esta lógica debería estar completamente en un método @Transactional del EncuestaService
     * para asegurar que si falla al crear una opción, la Encuesta también se deshaga (ROLLBACK).
     * Por simplicidad, se realiza aquí, pero se recomienda moverla.
     */
    @PostMapping
    // Marcamos la transacción aquí para que si falla un crearOpcion, se deshaga el crearEncuesta.
    @Transactional 
    public ResponseEntity<String> crearEncuestaConOpciones(
            // Asumo que recibes un JSON con la encuesta y la lista de opciones.
            @RequestBody CrearEncuestaRequest requestBody, 
            @RequestParam Long idUsuarioAuditoria) {
        try {
            // 1. CREAR LA ENCUESTA (Obtenemos el ID de la cabecera)
            Long nuevoIdEncuesta = encuestaService.crearEncuesta(requestBody.getEncuesta(), idUsuarioAuditoria); //

            // 2. CREAR LAS OPCIONES, asignándoles el nuevo ID
            if (requestBody.getOpciones() != null && !requestBody.getOpciones().isEmpty()) {
                int contador = 1;
                for (OpcionE opcion : requestBody.getOpciones()) {
                    // Asignamos el ID que acabamos de crear
                    opcion.setIdEncuesta(nuevoIdEncuesta);
                    
                    // Llamamos al servicio de opciones
                    String mensajeOpcion = opcionEService.crearOpcion(opcion, contador, idUsuarioAuditoria); //
                    
                    // Si el servicio devuelve un mensaje de error (por lógica de negocio), lanzamos excepción.
                    if (mensajeOpcion.startsWith("Error:")) {
                        throw new Exception(mensajeOpcion);
                    }
                    contador++;
                }
            } else {
                throw new Exception("Error: Una encuesta debe tener al menos una opción.");
            }
            
            return ResponseEntity.status(HttpStatus.CREATED).body("Encuesta y sus opciones creadas con éxito. ID: " + nuevoIdEncuesta);

        } catch (IllegalArgumentException e) {
            // Captura errores de validación de negocio (ej. pregunta vacía)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Captura errores de SQL o de flujo de opciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la encuesta: " + e.getMessage());
        }
    }

    // --- 2. ENDPOINT DE RESULTADOS (GET) ---

    /**
     * Endpoint para OBTENER los resultados/conteo de votos de una encuesta.
     * URL: HTTP GET /api/encuestas/5/resultados
     */
    @GetMapping("/{idEncuesta}/resultados")
    public ResponseEntity<List<EncuestaResultado>> obtenerResultados(@PathVariable Long idEncuesta) {
        
        // Llama al método que existe en tu servicio y que trae opciones y votos.
        List<EncuestaResultado> resultados = encuestaService.buscarEncuestaCompleta(idEncuesta); //
        
        if (resultados != null && !resultados.isEmpty()) {
            return ResponseEntity.ok(resultados);
        } else {
            // Podría ser que la encuesta no exista o no tenga opciones/votos
            return ResponseEntity.notFound().build();
        }
    }

    // --- 3. ENDPOINT DE ACCIÓN DE VOTACIÓN (POST) ---

    /**
     * Endpoint para REGISTRAR o ACTUALIZAR un voto.
     * URL: HTTP POST /api/encuestas/votar
     */
    @PostMapping("/votar")
    public ResponseEntity<String> registrarVoto(
            @RequestBody VotarO voto, // Contiene idOpcion, idEncuesta, idUsuario.
            @RequestParam Long idUsuarioAuditoria) {
        
        // Llama al servicio VotarO. El servicio maneja la lógica de voto único/actualización.
        String mensaje = votarOService.votarEnOpcion(
            voto.getIdOpcion(), 
            voto.getIdUsuario(),
            voto.getIdEncuesta(),
            idUsuarioAuditoria
        ); //
        
        // Si el mensaje del servicio indica un error, devolvemos 400.
        if (mensaje.startsWith("Error:")) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
        }

        // Si es exitoso, devolvemos 200 OK con el mensaje de éxito (ej. "Voto ingresado")
        return ResponseEntity.status(HttpStatus.OK).body(mensaje);
    }
    
    // --- DTO INTERNO PARA EL REQUEST DE CREACIÓN ---
    // Esta clase interna ayuda a Spring a mapear el JSON de la petición POST.
    static class CrearEncuestaRequest {
        private Encuesta encuesta;
        private List<OpcionE> opciones;

        public Encuesta getEncuesta() {
            return encuesta;
        }

        public void setEncuesta(Encuesta encuesta) {
            this.encuesta = encuesta;
        }

        public List<OpcionE> getOpciones() {
            return opciones;
        }

        public void setOpciones(List<OpcionE> opciones) {
            this.opciones = opciones;
        }
    }
}