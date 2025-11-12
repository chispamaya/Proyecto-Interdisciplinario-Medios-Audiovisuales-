package com.example.demo.service;

import com.example.demo.dto.AudienciaCon;
import com.example.demo.dto.ReporteAudienciaDTO; // (1) Importar el DTO de Reporte
import com.example.demo.repository.AudienciaConRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List; // (2) Importar List

/**
 * Capa de Servicio para la lógica de negocio de Likes/Dislikes (Audiencia).
 */
@Service
public class AudienciaConService {

    @Autowired
    private AudienciaConRepository audienciaConRepository;

    /**
     * Lógica para que un espectador dé Like/Dislike (SP 'ld').
     */
    public String crearOModificarVoto(AudienciaCon voto, Long idUsuarioAuditoria) {
        // Lógica de negocio (validaciones) iría aquí.
        return audienciaConRepository.crearOModificarVoto(voto, idUsuarioAuditoria);
    }

    /**
     * Lógica para que un espectador quite su voto (SP 'bv').
     */
    public String borrarVoto(Long idContenido, Long idUsuario, Long idUsuarioAuditoria) {
        // Lógica de negocio (validaciones) iría aquí.
        return audienciaConRepository.borrarVoto(idContenido, idUsuario, idUsuarioAuditoria);
    }

    /**
     * (NUEVO) Lógica para obtener el reporte de audiencia.
     * Esto será llamado por el Controller para 'ReportesAudencia.jsx'.
     *
     * @return Una lista de DTOs con los conteos por posteo.
     */
    public List<ReporteAudienciaDTO> obtenerReporteAudiencia() {
        // (3) Llama al nuevo método del repositorio.
        // Por ahora, solo pasamos los datos. Si necesitáramos
        // cruzarlo con el nombre del contenido (de 'contenidos'),
        // la lógica de negocio para eso iría aquí.
        return audienciaConRepository.getReporteAudiencia();
    }
}