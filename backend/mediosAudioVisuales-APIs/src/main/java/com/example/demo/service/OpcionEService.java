package com.example.demo.service;

import com.example.demo.dto.OpcionE;
import com.example.demo.repository.OpcionERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils; // Importamos la utilidad de Spring

/**
 * Capa de servicio para la lógica de negocio de las Opciones de Encuestas.
 */
@Service
public class OpcionEService {

    @Autowired
    private OpcionERepository opcionERepository;

    /**
     * Crea una nueva opción para una encuesta existente.
     *
     * @param opcion El DTO OpcionE que contiene el idEncuesta y el texto de la opción.
     * @param contador Un número (ej: 1, 2, 3...) para el mensaje de éxito del SP.
     * @param idUsuarioAuditoria El ID del usuario que ejecuta la acción (para logs).
     * @return Un mensaje de éxito ("Opcion 1 creada...") o un mensaje de error específico.
     */
    public String crearOpcion(OpcionE opcion, int contador, Long idUsuarioAuditoria) {
        
        // --- Lógica de Negocio (Validaciones) ---

        // 1. Validar el DTO principal
        if (opcion == null) {
            return "Error: No se proporcionaron datos de la opción.";
        }

        // 2. Validar el ID de la encuesta (clave foránea en el SP 'co')
        if (opcion.getIdEncuesta() == null || opcion.getIdEncuesta() <= 0) {
            return "Error: El ID de la encuesta al que pertenece esta opción es inválido.";
        }

        // 3. Validar el texto de la opción
        //    StringUtils.hasText() chequea si es null, "" (vacío), o " " (solo espacios)
        if (!StringUtils.hasText(opcion.getOpcion())) {
            return "Error: El texto de la opción no puede estar vacío.";
        }

        // 4. Validar el contador
        if (contador <= 0) {
            return "Error: El número de la opción (contador) debe ser un valor positivo.";
        }

        // 5. Validar el ID de auditoría (para el EXIT HANDLER del SP 'co')
        if (idUsuarioAuditoria == null || idUsuarioAuditoria <= 0) {
            return "Error: Falta el ID de auditoría del usuario.";
        }

        // --- Llamada al Repositorio ---
        // Si todo está bien, llamamos al repositorio
        return opcionERepository.crearOpcion(opcion, contador, idUsuarioAuditoria);
    }
}