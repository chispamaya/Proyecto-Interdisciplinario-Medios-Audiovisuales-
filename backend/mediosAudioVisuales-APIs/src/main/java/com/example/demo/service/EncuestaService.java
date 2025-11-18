package com.example.demo.service;

import com.example.demo.dto.Encuesta;
import com.example.demo.dto.EncuestaResultado;
import com.example.demo.dto.OpcionE;
import com.example.demo.repository.EncuestaRepository;
import com.example.demo.repository.OpcionERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class EncuestaService {

    @Autowired
    private EncuestaRepository encuestaRepository;
    
    @Autowired
    private OpcionERepository opcionERepository;

    @Transactional
    public String crearEncuestaConOpciones(Encuesta encuesta, List<OpcionE> opciones, Long idUsuarioAuditoria) throws Exception {
        // ... (Tu código de creación que ya tenías, sin cambios) ...
        // ... (Lo omito para ahorrar espacio, déjalo igual) ...
        
        // VALIDAR Y CREAR LA ENCUESTA
        if (encuesta == null || !StringUtils.hasText(encuesta.getPreguntar()) || encuesta.getIdUsuario() == null) {
            throw new IllegalArgumentException("Error: Datos de la encuesta (pregunta o idUsuario) inválidos.");
        }
        if (idUsuarioAuditoria == null || idUsuarioAuditoria <= 0) {
            throw new IllegalArgumentException("Error: Falta el ID de auditoría.");
        }
        
        Long nuevoIdEncuesta = encuestaRepository.crearEncuesta(encuesta, idUsuarioAuditoria);

        if (opciones == null || opciones.isEmpty()) {
            throw new IllegalArgumentException("Error: Una encuesta debe tener al menos una opción.");
        }

        int contador = 1;
        for (OpcionE opcion : opciones) {
            opcion.setIdEncuesta(nuevoIdEncuesta);
            
            if (!StringUtils.hasText(opcion.getOpcion())) {
                 throw new IllegalArgumentException("Error: El texto de la opción " + contador + " no puede estar vacío.");
            }

            String mensajeOpcion = opcionERepository.crearOpcion(opcion, contador, idUsuarioAuditoria);
            
            if (mensajeOpcion.startsWith("Error:")) {
                throw new Exception(mensajeOpcion);
            }
            contador++;
        }
        
        return "Encuesta y sus opciones creadas con éxito. ID: " + nuevoIdEncuesta;
    }

    public Long crearEncuesta(Encuesta encuesta, Long idUsuarioAuditoria) {
        // ... (Tu código original, sin cambios) ...
        return encuestaRepository.crearEncuesta(encuesta, idUsuarioAuditoria);
    }

    /**
     * (MODIFICADO)
     * Antes bloqueaba si idEncuesta era null.
     * Ahora PERMITE null, porque null significa "Traer Todas".
     */
    public List<EncuestaResultado> buscarEncuestaCompleta(Long idEncuesta) {
        // Si el ID viene con 0 o negativo, devolvemos lista vacía (error de validación).
        // Pero si es NULL, LO DEJAMOS PASAR.
        if (idEncuesta != null && idEncuesta <= 0) {
            return Collections.emptyList();
        }
        
        // Llamamos al repositorio. Si idEncuesta es null, JDBC lo enviará como SQL NULL,
        // y tu SP modificado entenderá que debe devolver todo.
        return encuestaRepository.buscarEncuestaConOpcionesYVotos(idEncuesta);
    }
}