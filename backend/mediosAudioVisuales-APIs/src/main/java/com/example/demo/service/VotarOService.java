package com.example.demo.service;

import com.example.demo.repository.VotarORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Capa de servicio para la lógica de negocio de las votaciones.
 */
@Service
public class VotarOService {

    @Autowired
    private VotarORepository votarORepository;

    /**
     * Procesa la acción de un usuario al votar en una encuesta.
     * Llama al SP 'vo' a través del repositorio.
     *
     * @param idOpcion El ID de la opción (opcion_e) que se está votando.
     * @param idUsuario El ID del usuario (usuario) que realiza el voto.
     * @param idEncuesta El ID de la encuesta (encuesta) a la que pertenece la opción.
     * @param idUsuarioAuditoria El ID del usuario que ejecuta la acción (para logs de auditoría/errores).
     * @return Un mensaje de éxito ("Voto ingresado", "Voto eliminado", "Voto actualizado") o de error.
     */
    public String votarEnOpcion(Long idOpcion, Long idUsuario, Long idEncuesta, Long idUsuarioAuditoria) {
        
        // --- Lógica de Negocio (Validación de Entradas) ---
        // Nos aseguramos de que NINGÚN ID sea nulo o inválido ANTES de
        // intentar pasárselo a la base de datos.
        if (idOpcion == null || idOpcion <= 0) {
            return "Error: El ID de la opción no puede ser nulo o vacío.";
        }
        if (idUsuario == null || idUsuario <= 0) {
            return "Error: El ID del usuario que vota no puede ser nulo o vacío.";
        }
        if (idEncuesta == null || idEncuesta <= 0) {
            return "Error: El ID de la encuesta no puede ser nulo o vacío.";
        }
        if (idUsuarioAuditoria == null || idUsuarioAuditoria <= 0) {
            // Este es un error de sistema, no del usuario, pero igual lo validamos
            return "Error: Falta el ID de auditoría del usuario.";
        }

        // --- Llamada al Repositorio ---
        // Si todas las validaciones pasan, llamamos al método del repositorio.
        //
        return votarORepository.votarOpcion(idOpcion, idUsuario, idEncuesta, idUsuarioAuditoria);
    }
}