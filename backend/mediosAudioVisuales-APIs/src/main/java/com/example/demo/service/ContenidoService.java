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
     * Lógica para subir contenido (SP 'cc').
     */
    public String crearContenido(Contenido contenido, Long idUsuarioAuditoria) {
        // Validación básica: si hay ruta de archivo, el formato es obligatorio.
        if (contenido.getRutaArchivo() != null && !contenido.getRutaArchivo().isEmpty()) {
             if(contenido.getFormato() == null || contenido.getFormato().isEmpty()) {
                 throw new IllegalArgumentException("Si sube un archivo, debe especificar el formato.");
             }
        }
        return contenidoRepository.crearContenido(contenido, idUsuarioAuditoria);
    }

    /**
     * Lógica para borrar contenido (SP 'bc').
     */
    public String borrarContenido(Long idContenido, Long idUsuarioAuditoria) {
        return contenidoRepository.borrarContenido(idContenido, idUsuarioAuditoria);
    }

    /**
     * Lógica para listar todo.
     */
    public List<Contenido> listarTodosLosContenidos() {
        return contenidoRepository.listarTodosLosContenidos();
    }

    /**
     * Lógica para listar por usuario.
     */
    public List<Contenido> listarContenidosPorUsuario(Long idUsuario) {
        return contenidoRepository.listarContenidosPorUsuario(idUsuario);
    }
}