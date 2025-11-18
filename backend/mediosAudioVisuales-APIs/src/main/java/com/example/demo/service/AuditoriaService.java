package com.example.demo.service;

import com.example.demo.dto.Auditoria;
import com.example.demo.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de Servicio para la lógica de negocio de Auditoría.
 */
@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    /**
     * Obtener TODOS los registros de auditoría.
     */
    public List<Auditoria> listarAuditorias() {
        return auditoriaRepository.obtenerTodasLasAuditorias();
    }

    /**
     * (MÉTODO AÑADIDO) Filtrar por Tabla y Acción.
     * Expone el método del Repository al Controller.
     */
    public List<Auditoria> filtrarPorTablaYAccion(String tabla, String accion) {
        // Llama al método que creaste en el Repositorio
        return auditoriaRepository.buscarAuditoriaPorTablaYAccion(tabla, accion);
    }

    /**
     * (MÉTODO AÑADIDO) Filtrar por Usuario, Tabla y Acción.
     * Expone el método del Repository al Controller.
     */
    public List<Auditoria> filtrarPorUsuarioTablaYAccion(Long idUsuario, String tabla, String accion) {
        // Llama al método que creaste en el Repositorio
        return auditoriaRepository.buscarAuditoriaPorUsuarioYTTabla(idUsuario, tabla, accion);
    }
}