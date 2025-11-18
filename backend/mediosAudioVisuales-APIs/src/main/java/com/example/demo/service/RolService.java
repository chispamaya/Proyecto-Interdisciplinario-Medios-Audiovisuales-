package com.example.demo.service;

import com.example.demo.dto.Permisos;
import com.example.demo.dto.PermisosRol;
import com.example.demo.dto.Rol;
import com.example.demo.repository.PermisosRepository;
import com.example.demo.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    // Inyectamos el repo de permisos para saber qué tiene cada rol
    @Autowired
    private PermisosRepository permisosRepository;

    /**
     * Devuelve todos los roles, pero enriquecidos con la lista de sus permisos
     * para que el frontend pueda distinguirlos.
     */
    public List<Rol> listarTodosLosRoles() {
        
        // 1. Traemos Roles, Permisos y Relaciones
        List<Rol> roles = rolRepository.listarTodosLosRoles();
        List<Permisos> permisos = permisosRepository.listarTodosLosPermisos();
        List<PermisosRol> relaciones = permisosRepository.listarTodasLasRelacionesPermisosRol();

        // 2. Mapa de Permisos (ID -> Nombre)
        Map<Long, String> mapaPermisos = permisos.stream()
                .collect(Collectors.toMap(Permisos::getId, Permisos::getTipoPermiso));

        // 3. Mapa de Relaciones (ID Rol -> Lista de IDs Permisos)
        Map<Long, List<Long>> mapaRelaciones = new HashMap<>();
        for (PermisosRol rel : relaciones) {
            mapaRelaciones.putIfAbsent(rel.getIdRol(), new ArrayList<>());
            mapaRelaciones.get(rel.getIdRol()).add(rel.getIdPermiso());
        }

        // 4. Combinamos
        for (Rol rol : roles) {
            List<Long> idsPermisosDelRol = mapaRelaciones.get(rol.getId());
            List<String> nombresPermisos = new ArrayList<>();

            if (idsPermisosDelRol != null) {
                for (Long idPermiso : idsPermisosDelRol) {
                    if (mapaPermisos.containsKey(idPermiso)) {
                        nombresPermisos.add(mapaPermisos.get(idPermiso));
                    }
                }
            }
            // Guardamos la lista en el DTO
            rol.setDetallesPermisos(nombresPermisos);
        }

        return roles;
    }
    
    public Rol buscarRolPorId(Long id) {
        return rolRepository.buscarRolPorId(id);
    }
}