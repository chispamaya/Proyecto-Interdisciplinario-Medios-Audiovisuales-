package com.example.demo.service;

import com.example.demo.dto.Auditoria;
import com.example.demo.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Capa de Servicio para la lógica de negocio de Auditoría.
 * Se encarga de exponer los logs del sistema para uso administrativo
 * (para la página 'Auditoria.jsx').
 */
@Service
public class AuditoriaService {

    // 1. Inyección de Dependencias
    @Autowired
    private AuditoriaRepository auditoriaRepository;

    /**
     * Lógica de negocio para obtener TODOS los registros de auditoría.
     *
     * @return Una lista de todos los registros de la tabla 'auditoria'.
     */
    public List<Auditoria> listarAuditorias() {
        
        // 2. Lógica de Negocio (Filtrado)
        // Por ahora, solo devolvemos la lista completa.
        // En el FUTURO, aquí podrías añadir lógica para filtrar por usuario o fecha.
        
        // 3. Llamada al Repositorio
        return auditoriaRepository.obtenerTodasLasAuditorias();
    }
}