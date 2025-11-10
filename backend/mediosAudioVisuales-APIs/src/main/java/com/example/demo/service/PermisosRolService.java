package com.example.demo.service;

import com.example.demo.dto.PermisosRol;
import com.example.demo.repository.PermisosRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Capa de servicio para la lógica de negocio relacionada con PermisosRol.
 */
@Service
public class PermisosRolService {

    // Inyectamos el repositorio que se encargará de hablar con la BD
    @Autowired
    private PermisosRolRepository permisosRolRepository;

    /**
     * Obtiene la lista completa de todas las asignaciones de permisos a roles.
     * * @return Una lista de objetos PermisosRol.
     */
    public List<PermisosRol> listarTodasLasAsignaciones() {
        
        // En este caso, el servicio no tiene lógica de negocio extra (como filtros
        // o validaciones), así que simplemente llama al repositorio y devuelve el resultado.
        
        return permisosRolRepository.listarTodasLasAsignaciones();
    }

}