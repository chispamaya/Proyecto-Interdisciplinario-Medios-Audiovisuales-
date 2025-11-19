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
        
        // Validaciones básicas
        if (encuesta == null || !StringUtils.hasText(encuesta.getPreguntar()) || encuesta.getIdUsuario() == null) {
            throw new IllegalArgumentException("Error: Datos de la encuesta incompletos.");
        }
        if (idUsuarioAuditoria == null) {
            throw new IllegalArgumentException("Error: Falta el ID de auditoría.");
        }
        if (opciones == null || opciones.isEmpty()) {
            throw new IllegalArgumentException("Error: La encuesta debe tener al menos una opción.");
        }

        // 1. CREAR ENCUESTA (Ahora lanzará excepción si falla)
        Long nuevoIdEncuesta = encuestaRepository.crearEncuesta(encuesta, idUsuarioAuditoria);
        
        // Si llegamos aquí, nuevoIdEncuesta TIENE VALOR
        int contador = 1;
        for (OpcionE opcion : opciones) {
            opcion.setIdEncuesta(nuevoIdEncuesta);
            
            if (!StringUtils.hasText(opcion.getOpcion())) {
                 throw new IllegalArgumentException("Error: La opción " + contador + " está vacía.");
            }

            String mensajeOpcion = opcionERepository.crearOpcion(opcion, contador, idUsuarioAuditoria);
            
            if (mensajeOpcion.startsWith("Error:") || mensajeOpcion.contains("Ocurrio un error")) {
                throw new Exception("Fallo al crear opción " + contador + ": " + mensajeOpcion);
            }
            contador++;
        }
        
        return "Encuesta creada con éxito. ID: " + nuevoIdEncuesta;
    }

    // Métodos de lectura
    public List<EncuestaResultado> buscarEncuestaCompleta(Long idEncuesta) {
        if (idEncuesta != null && idEncuesta <= 0) return Collections.emptyList();
        return encuestaRepository.buscarEncuestaConOpcionesYVotos(idEncuesta);
    }
}