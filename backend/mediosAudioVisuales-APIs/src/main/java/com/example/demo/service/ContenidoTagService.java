package com.example.demo.service;

import com.example.demo.dto.contenidoTag;
import com.example.demo.repository.ContenidoTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // (1) Importar Transactional

import java.util.List;

/**
 * Capa de Servicio para la lógica de negocio de Contenido-Tags.
 * Se encarga de la asignación y gestión de etiquetas a los contenidos.
 */
@Service
public class ContenidoTagService {

    // 2. Inyección de Dependencias
    // Spring inyecta el repositorio que maneja la tabla 'contenido_tag'.
    @Autowired
    private ContenidoTagRepository contenidoTagRepository;

    /**
     * Lógica de negocio para asignar UN tag a UN contenido (SP 'cct').
     *
     * @param link El DTO que contiene idContenido y idTag.
     * @param idUsuarioAuditoria El ID del usuario que realiza la acción.
     * @return Mensaje de éxito o error.
     */
    public String asignarTag(contenidoTag link, Long idUsuarioAuditoria) {
        // (Lógica de validación futura iría aquí)
        // Ej: Verificar que el ID del tag exista, verificar que el ID de contenido exista.
        
        return contenidoTagRepository.crearContenidoTag(link, idUsuarioAuditoria);
    }

    /**
     * Lógica de negocio para eliminar TODOS los tags de UN contenido (SP 'bct').
     *
     * @param idContenido El ID del contenido al que se le limpiarán los tags.
     * @param idUsuarioAuditoria El ID del usuario que realiza la acción.
     * @return Mensaje de éxito o error.
     */
    public String eliminarTagsDeContenido(Long idContenido, Long idUsuarioAuditoria) {
        return contenidoTagRepository.borrarTagsPorContenido(idContenido, idUsuarioAuditoria);
    }

    /**
     * (LÓGICA DE NEGOCIO AVANZADA)
     * Actualiza la lista completa de tags para un contenido.
     * Esto es lo que el frontend ('GestionMultimedia.jsx') probablemente usará.
     *
     * @param idContenido El ID del contenido a modificar.
     * @param idTagsNuevos Una lista de los IDs de los tags que debe tener (ej: [1, 3, 5]).
     * @param idUsuarioAuditoria El ID del usuario que realiza la acción.
     */
    // (3) Anotación Transactional
    @Transactional
    public void actualizarTagsParaContenido(Long idContenido, List<Long> idTagsNuevos, Long idUsuarioAuditoria) {
        
        // --- Esta es la Lógica de Negocio ---
        // Combina los dos métodos del repositorio en una sola operación segura.

        // 1. Borramos todos los tags antiguos asociados a ese contenido (SP 'bct')
        contenidoTagRepository.borrarTagsPorContenido(idContenido, idUsuarioAuditoria);

        // 2. Si la nueva lista no está vacía, asignamos los nuevos tags uno por uno (SP 'cct')
        if (idTagsNuevos != null && !idTagsNuevos.isEmpty()) {
            
            for (Long idTag : idTagsNuevos) {
                contenidoTag nuevoLink = new contenidoTag();
                nuevoLink.setIdContenido(idContenido);
                nuevoLink.setIdTag(idTag);
                
                // 3. Asignamos el tag
                contenidoTagRepository.crearContenidoTag(nuevoLink, idUsuarioAuditoria);
            }
        }
        
        // (4) Fin de la transacción
        // Si algo falla (ej. uno de los 'crear' falla), @Transactional
        // deshará automáticamente el 'borrar' y los 'crear' anteriores.
    }
}