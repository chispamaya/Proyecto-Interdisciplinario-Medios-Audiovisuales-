package com.example.demo.service;

import com.example.demo.dto.Contenido;
import com.example.demo.repository.ContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Capa de Servicio para la lógica de negocio de Contenidos (Posteos).
 * Conecta el frontend (Subida, Gestión, Aprobación) con el Repositorio.
 */
@Service
public class ContenidoService {

    // 1. Inyección de Dependencias del Repositorio
    @Autowired
    private ContenidoRepository contenidoRepository;

    /**
     * Lógica para SUBIR un nuevo contenido (SP 'cc').
     * Llamado desde 'SubidaMultimedia.jsx'.
     */
    public String crearContenido(Contenido contenido, Long idUsuarioAuditoria) {
        // Aquí podríamos validar que el 'tipo' sea 'programa', 'audio' o 'informe'
        if (contenido.getTitulo() == null || contenido.getTitulo().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        // (Más validaciones de negocio irían aquí)
        
        return contenidoRepository.crearContenido(contenido, idUsuarioAuditoria);
    }

    /**
     * Lógica para BORRAR un contenido (SP 'bc').
     * Llamado desde 'GestionMultimedia.jsx'.
     */
    public String borrarContenido(Long idContenido, Long idUsuarioAuditoria) {
        // Aquí podríamos verificar que el contenido no esté "en vivo" antes de borrarlo.
        return contenidoRepository.borrarContenido(idContenido, idUsuarioAuditoria);
    }

    /**
     * Lógica para APROBAR/RECHAZAR un contenido (SP 'mces').
     * Llamado desde 'EstadoAprobacion.jsx'.
     */
    public String modificarEstadoContenido(Long idContenido, String nuevoEstado, Long idUsuarioAuditoria) {
        // Validamos que el estado sea uno de los permitidos
        if (!"Aprobado".equals(nuevoEstado) && !"Rechazado".equals(nuevoEstado)) {
            throw new IllegalArgumentException("Estado no válido. Solo se permite 'Aprobado' o 'Rechazado'.");
        }
        return contenidoRepository.modificarEstadoContenido(idContenido, nuevoEstado, idUsuarioAuditoria);
    }

    /**
     * Lógica para OBTENER TODO el contenido.
     * Llamado desde 'EstadoAprobacion.jsx'.
     */
    public List<Contenido> listarTodosLosContenidos() {
        return contenidoRepository.listarTodosLosContenidos();
    }

    /**
     * Lógica para OBTENER el contenido DE UN USUARIO (SP 's').
     * Llamado desde 'GestionMultimedia.jsx' (que es "Tu contenido").
     */
    public List<Contenido> listarContenidosPorUsuario(Long idUsuario) {
        return contenidoRepository.listarContenidosPorUsuario(idUsuario);
    }

    /**
     * (EXTRA) Lógica para filtrar por TIPO, como en el frontend.
     * Llamado desde 'GestionMultimedia.jsx'.
     */
    public List<Contenido> listarContenidosPorUsuarioYTipo(Long idUsuario, String tipo) {
        // 1. Obtenemos todos los contenidos del usuario desde la BD
        List<Contenido> contenidosDelUsuario = contenidoRepository.listarContenidosPorUsuario(idUsuario);
        
        // 2. Filtramos la lista en Java (Lógica de Negocio)
        return contenidosDelUsuario.stream()
                .filter(contenido -> contenido.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }
}