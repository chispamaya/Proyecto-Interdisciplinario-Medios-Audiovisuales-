package com.example.demo.service;

import com.example.demo.dto.Encuesta;
import com.example.demo.dto.EncuestaResultado;
import com.example.demo.dto.OpcionE; // (CAMBIO) Importar DTO de Opción
import com.example.demo.repository.EncuestaRepository;
import com.example.demo.repository.OpcionERepository; // (CAMBIO) Importar Repositorio de Opción
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // (CAMBIO) Importar Transactional
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class EncuestaService {

    @Autowired
    private EncuestaRepository encuestaRepository;
    
    // (CAMBIO) Inyectar el Repositorio de Opciones
    @Autowired
    private OpcionERepository opcionERepository;

    /**
     * (NUEVO MÉTODO)
     * Lógica de negocio COMPLETA y TRANSACCIONAL para crear una encuesta
     * y todas sus opciones. Esto asegura que la auditoría funcione.
     */
    @Transactional // (CAMBIO) La transacción ahora vive aquí.
    public String crearEncuestaConOpciones(Encuesta encuesta, List<OpcionE> opciones, Long idUsuarioAuditoria) throws Exception {
        
        // 1. VALIDAR Y CREAR LA ENCUESTA
        if (encuesta == null || !StringUtils.hasText(encuesta.getPreguntar()) || encuesta.getIdUsuario() == null) {
            throw new IllegalArgumentException("Error: Datos de la encuesta (pregunta o idUsuario) inválidos.");
        }
        if (idUsuarioAuditoria == null || idUsuarioAuditoria <= 0) {
            throw new IllegalArgumentException("Error: Falta el ID de auditoría.");
        }
        
        // Llama al SP 'cen' para crear la cabecera
        Long nuevoIdEncuesta = encuestaRepository.crearEncuesta(encuesta, idUsuarioAuditoria);

        // 2. VALIDAR Y CREAR LAS OPCIONES
        if (opciones == null || opciones.isEmpty()) {
            // Si no hay opciones, fallamos para que @Transactional deshaga la encuesta.
            throw new IllegalArgumentException("Error: Una encuesta debe tener al menos una opción.");
        }

        int contador = 1;
        for (OpcionE opcion : opciones) {
            opcion.setIdEncuesta(nuevoIdEncuesta);
            
            if (!StringUtils.hasText(opcion.getOpcion())) {
                 throw new IllegalArgumentException("Error: El texto de la opción " + contador + " no puede estar vacío.");
            }

            // Llama al SP 'co' para crear la opción
            String mensajeOpcion = opcionERepository.crearOpcion(opcion, contador, idUsuarioAuditoria);
            
            if (mensajeOpcion.startsWith("Error:")) {
                // Si el SP falla, @Transactional deshará todo (incluida la encuesta).
                throw new Exception(mensajeOpcion);
            }
            contador++;
        }
        
        // Si todo salió bien, Spring hace COMMIT aquí y los Triggers funcionan.
        return "Encuesta y sus opciones creadas con éxito. ID: " + nuevoIdEncuesta;
    }


    // --- Métodos Originales (sin cambios) ---
    
    public Long crearEncuesta(Encuesta encuesta, Long idUsuarioAuditoria) {
        // ... (Tu código de validación original) ...
        if (encuesta == null) {
            throw new IllegalArgumentException("Error: No se proporcionaron datos de la encuesta.");
        }
        if (!StringUtils.hasText(encuesta.getPreguntar())) {
            throw new IllegalArgumentException("Error: El texto de la pregunta no puede estar vacío.");
        }
        if (encuesta.getIdUsuario() == null || encuesta.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("Error: El ID del usuario creador es inválido.");
        }
        if (idUsuarioAuditoria == null || idUsuarioAuditoria <= 0) {
            throw new IllegalArgumentException("Error: Falta el ID de auditoría del usuario.");
        }
        return encuestaRepository.crearEncuesta(encuesta, idUsuarioAuditoria);
    }

    public List<EncuestaResultado> buscarEncuestaCompleta(Long idEncuesta) {
        if (idEncuesta == null || idEncuesta <= 0) {
            return Collections.emptyList();
        }
        return encuestaRepository.buscarEncuestaConOpcionesYVotos(idEncuesta);
    }
}