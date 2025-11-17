package com.example.demo.service;

import com.example.demo.dto.Contenido;
import com.example.demo.repository.ContenidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContenidoService {

    @Autowired
    private ContenidoRepository contenidoRepository;

    /**
     * Subir contenido (SP 'cc').
     */
    public String crearContenido(Contenido contenido, Long idUsuarioAuditoria) {
        // Validaciones simples compatibles con la BD actual
        if (contenido.getRutaArchivo() == null || contenido.getRutaArchivo().isEmpty()) {
             // Si es solo texto, quizás no necesite ruta, depende de tu regla de negocio.
             // Por ahora validamos que al menos haya formato.
             if(contenido.getFormato() == null) throw new IllegalArgumentException("El formato es obligatorio");
        }
        
        return contenidoRepository.crearContenido(contenido, idUsuarioAuditoria);
    }

    /**
     * Borrar contenido (SP 'bc').
     */
    public String borrarContenido(Long idContenido, Long idUsuarioAuditoria) {
        return contenidoRepository.borrarContenido(idContenido, idUsuarioAuditoria);
    }

    /**
     * Listar TODO.
     */
    public List<Contenido> listarTodosLosContenidos() {
        return contenidoRepository.listarTodosLosContenidos();
    }

    /**
     * Listar por usuario.
     */
    public List<Contenido> listarContenidosPorUsuario(Long idUsuario) {
        return contenidoRepository.listarContenidosPorUsuario(idUsuario);
    }
    
    // NOTA: Se eliminaron 'modificarEstadoContenido' y 'listarContenidosPorUsuarioYTipo'
    // porque la BD no soporta 'estado' ni 'tipo'.
}