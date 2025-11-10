package com.example.demo.service;

import com.example.demo.dto.Errores;
import com.example.demo.repository.ErroresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de servicio para la lógica de negocio relacionada con los Errores.
 */
@Service
public class ErroresService {

    @Autowired
    private ErroresRepository erroresRepository;

    /**
     * Obtiene una lista de todos los errores registrados en la base de datos.
     *
     * @return Una lista de objetos Errores.
     */
    public List<Errores> listarTodosLosErrores() {
        
        // El repositorio llama a "CALL s('errores', null, @mensaje)"
        // que ejecuta un "SELECT * FROM errores".
        return erroresRepository.listarTodosLosErrores();
    }

    /*
     * A futuro, aquí podrías agregar la lógica de negocio para
     * filtrar o procesar estos errores. Por ejemplo:
     *
     * public List<Errores> listarErroresPorUsuario(Long idUsuario) { ... }
     * public List<Errores> listarErroresPorFecha(Date inicio, Date fin) { ... }
     * public void eliminarErroresAntiguos() { ... }
     */
}