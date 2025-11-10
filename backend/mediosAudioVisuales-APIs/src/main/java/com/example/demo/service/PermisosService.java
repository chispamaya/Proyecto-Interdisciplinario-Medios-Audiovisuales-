package com.example.demo.service;

import com.example.demo.dto.Permisos;
import com.example.demo.repository.PermisosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils; // Importante para validar

import java.util.Collections;
import java.util.List;

/**
 * Capa de servicio para la lógica de negocio relacionada con Permisos.
 */
@Service
public class PermisosService {

    @Autowired
    private PermisosRepository permisosRepository;

    /**
     * Obtiene la lista completa de TODOS los tipos de permisos
     * disponibles en el sistema (como objetos Permisos, con ID y String).
     *
     * @return Una lista de todos los objetos Permisos.
     */
    public List<Permisos> listarTodosLosPermisos() {
        
        // Llama al método del repositorio que devuelve List<Permisos>
        return permisosRepository.listarTodosLosPermisos();
    }

    /**
     * Obtiene la lista de NOMBRES de permisos (Strings) 
     * para un nombre de rol específico.
     * (ej: "HACER-TODO", "SUBIR_CONTENIDO")
     *
     * @param rol El nombre del rol (ej: "Administrador").
     * @return Una lista de Strings. Si el rol es nulo o vacío, 
     * devuelve una lista vacía.
     */
    public List<String> buscarPermisosPorRol(String rol) {

        // --- Lógica de Negocio (Validación de Entrada) ---
        // No tiene sentido buscar un rol nulo o vacío.
        if (!StringUtils.hasText(rol)) {
            return Collections.emptyList(); // Devolvemos lista vacía
        }

        // --- Llamada al Repositorio ---
        // Llama al método correcto del repo que devuelve List<String>
        return permisosRepository.buscarPermisosPorRol(rol);
    }
}