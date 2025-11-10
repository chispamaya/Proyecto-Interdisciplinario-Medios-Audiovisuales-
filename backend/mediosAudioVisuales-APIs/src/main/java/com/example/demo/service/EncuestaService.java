package com.example.demo.service;

import com.example.demo.dto.Encuesta;
import com.example.demo.dto.EncuestaResultado;
import com.example.demo.repository.EncuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Capa de servicio para la lógica de negocio de las Encuestas.
 */
@Service
public class EncuestaService {

    @Autowired
    private EncuestaRepository encuestaRepository;

    /**
     * Crea una nueva encuesta.
     * Valida los datos y llama al SP 'cen'
     *
     * @param encuesta El DTO Encuesta con la pregunta y el id del creador.
     * @param idUsuarioAuditoria El ID del usuario que ejecuta la acción (para logs).
     * @return El ID (Long) de la encuesta recién creada.
     * @throws IllegalArgumentException Si los datos de entrada no son válidos.
     */
    public Long crearEncuesta(Encuesta encuesta, Long idUsuarioAuditoria) {
        
        // --- Lógica de Negocio (Validaciones) ---
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
            // Previene el "error dentro del error" en el SP 'cen'
            throw new IllegalArgumentException("Error: Falta el ID de auditoría del usuario.");
        }

        // --- Llamada al Repositorio ---
        // Llama al repositorio, que devuelve el nuevo ID
        return encuestaRepository.crearEncuesta(encuesta, idUsuarioAuditoria);
    }

    /**
     * Busca una encuesta por su ID y trae sus opciones y el conteo de votos.
     * Llama al SP 's'
     *
     * @param idEncuesta El ID de la encuesta a buscar.
     * @return Una lista de EncuestaResultado. Si no se encuentra, devuelve una lista vacía.
     */
    public List<EncuestaResultado> buscarEncuestaCompleta(Long idEncuesta) {

        // --- Lógica de Negocio (Validación) ---
        if (idEncuesta == null || idEncuesta <= 0) {
            // No tiene sentido buscar un ID inválido.
            // Devolvemos una lista vacía para evitar NullPointerExceptions.
            return Collections.emptyList();
        }

        // --- Llamada al Repositorio ---
        return encuestaRepository.buscarEncuestaConOpcionesYVotos(idEncuesta);
    }
}