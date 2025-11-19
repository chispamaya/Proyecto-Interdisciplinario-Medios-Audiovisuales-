package com.example.demo.service;

import com.example.demo.dto.EmpleadoDto;
import com.example.demo.dto.PerfilDTO;
import com.example.demo.dto.Rol;
import com.example.demo.dto.Usuario;
import com.example.demo.dto.Permisos;
import com.example.demo.dto.PermisosRol;
import com.example.demo.dto.Auditoria;
import com.example.demo.dto.GestionProgramaDTO;
import com.example.demo.dto.Programa;

import com.example.demo.repository.PermisosRepository;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.ProgramaRepository;
import com.example.demo.repository.AuditoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.Duration;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PermisosRepository permisosRepository;
    
    @Autowired
    private ProgramaRepository programaRepository; 

    @Autowired
    private AuditoriaRepository auditoriaRepository;
    
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodosLosUsuarios();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.buscarUsuarioPorId(id);
    }

    public String crearUsuario(Usuario nuevoUsuario, Long idUsuarioQueCrea) {
    	Usuario usuarioExistente = usuarioRepository.buscarUsuarioPorEmail(nuevoUsuario.getEmail());
    	
    	if (usuarioExistente != null) {
            return "Error: El email '" + nuevoUsuario.getEmail() + "' ya está registrado.";
        }        
    	if (nuevoUsuario.getContrasenia().length() < 8) {
            return "Error: La contraseña debe tener al menos 8 caracteres.";
        }
    	
    	return usuarioRepository.crearUsuario(nuevoUsuario, idUsuarioQueCrea);
    }
  
    public String borrarUsuario(Long idUsuarioABorrar, Long idUsuarioQueBorra) {
        return usuarioRepository.borrarUsuario(idUsuarioABorrar, idUsuarioQueBorra);
    }
    
    /**
     * Lógica para cambiar SOLO el rol (preservando contraseña)
     */
    public String modificarRolUsuario(Long idUsuarioAModificar, Long idNuevoRol, Long idUsuarioQueModifica) {
        // 1. Buscamos al usuario para obtener su contraseña actual
        Usuario usuarioActual = usuarioRepository.buscarUsuarioPorId(idUsuarioAModificar);
        
        if (usuarioActual == null) {
            return "Error: No se encontró el usuario a modificar.";
        }

        // 2. Llamamos al nuevo método del repo pasando:
        //    - ID
        //    - Contraseña ACTUAL (para no perderla)
        //    - NUEVO Rol
        //    - Auditor
        return usuarioRepository.actualizarUsuario(
            idUsuarioAModificar, 
            usuarioActual.getContrasenia(), // <-- IMPORTANTE: Mantenemos la pass
            idNuevoRol, 
            idUsuarioQueModifica
        );
    }
    
    /**
     * Lógica para cambiar SOLO la contraseña (preservando rol)
     */
    public String cambiarContrasenia(Long idUsuario, String nuevaPassword) {
        // 1. Buscamos al usuario para obtener su rol actual
        Usuario usuarioActual = usuarioRepository.buscarUsuarioPorId(idUsuario);
        
        if (usuarioActual == null) {
            return "Error: Usuario no encontrado.";
        }

        // 2. Validaciones básicas (opcional, pero recomendado)
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            return "Error: La contraseña no puede estar vacía.";
        }

        // 3. Llamamos al repo preservando el rol
        // Usamos el mismo usuario como auditor (idUsuario)
        return usuarioRepository.actualizarUsuario(
            idUsuario, 
            nuevaPassword, 
            usuarioActual.getIdRol(), // <-- IMPORTANTE: Mantenemos el rol
            idUsuario 
        );
    }
    
	public List<GestionProgramaDTO> listarGestionProgramasPorUsuario(Long idUsuario) {
	        List<Programa> todosLosProgramas = programaRepository.listarTodosLosProgramas();
	        List<Auditoria> auditorias = auditoriaRepository.buscarAuditoriaPorUsuarioYTTabla(
	            idUsuario, 
	            "programas", 
	            "INSERT"
	        );
	        Map<Long, Programa> mapaProgramas = todosLosProgramas.stream()
	                .collect(Collectors.toMap(Programa::getId, programa -> programa));
	
	        List<GestionProgramaDTO> resultadoFinal = new ArrayList<>();
	
	        for (Auditoria aud : auditorias) {
	            Programa programa = mapaProgramas.get(aud.getRegistroAfectadoId());
	            if (programa != null) {
	                GestionProgramaDTO dto = new GestionProgramaDTO();
	                dto.setIdPrograma(programa.getId()); 
	                dto.setTitulo(programa.getNombre());
	                dto.setEstadoAprobacion(programa.getEstadoAprobacion());
	                dto.setFechaCreacion(aud.getFecha()); 
	                
	                if (programa.getHoraInicio() != null && programa.getHoraFin() != null) {
	                    long duracionEnMinutos = Duration.between(programa.getHoraInicio(), programa.getHoraFin()).toMinutes();
	                    dto.setDuracionEnMinutos(duracionEnMinutos); 
	                } else {
	                    dto.setDuracionEnMinutos(0L); 
	                }
	                resultadoFinal.add(dto);
	            }
	        }
	        return resultadoFinal; 
	    }

	public Usuario login(String email, String contrasenia) {
	        Usuario usuarioEnDB = usuarioRepository.buscarUsuarioPorEmail(email);
	        if (usuarioEnDB == null) {
	            return null; 
	        }
	        if (usuarioEnDB.getContrasenia().equals(contrasenia)) {
	            return usuarioEnDB;
	        } else {
	            return null;
	        }
	    }

	public PerfilDTO obtenerDatosPerfil(Long idUsuario) {
	        Usuario usuario = usuarioRepository.buscarUsuarioPorId(idUsuario);
	        if (usuario == null) { return null; }

	        Rol rol = rolRepository.buscarRolPorId(usuario.getIdRol());
	        if (rol == null) { return null; }
	
	        PerfilDTO perfil = new PerfilDTO();
	        perfil.setNombreUsuario(usuario.getNombre());
	        perfil.setNombreRol(rol.getNombre());     
	        return perfil;
	    }
	
	public List<EmpleadoDto> listarEmpleadosConPermisos() {
	        List<Usuario> usuarios = usuarioRepository.listarTodosLosUsuarios();
	        List<Rol> roles = rolRepository.listarTodosLosRoles();
	        List<Permisos> permisos = permisosRepository.listarTodosLosPermisos(); 
	        List<PermisosRol> relaciones = permisosRepository.listarTodasLasRelacionesPermisosRol();
	
	        Map<Long, String> mapaDeRoles = roles.stream()
	                .collect(Collectors.toMap(Rol::getId, Rol::getNombre));
	       
	        Map<Long, String> mapaDePermisos = permisos.stream()
	                .collect(Collectors.toMap(Permisos::getId, Permisos::getTipoPermiso));
	        
	        Map<Long, List<Long>> mapaRelaciones = new HashMap<>();
	        for (PermisosRol rel : relaciones) {
	            mapaRelaciones.putIfAbsent(rel.getIdRol(), new ArrayList<>());
	            mapaRelaciones.get(rel.getIdRol()).add(rel.getIdPermiso());
	        }
	
	        List<EmpleadoDto> resultadoFinal = new ArrayList<>();
	      
	        for (Usuario usuario : usuarios) {
	            EmpleadoDto dto = new EmpleadoDto();
	            dto.setId(usuario.getId());
	            dto.setNombre(usuario.getNombre());
	            dto.setEmail(usuario.getEmail());
	            dto.setNombreRol(mapaDeRoles.get(usuario.getIdRol()));
	
	            List<String> nombresDePermisos = new ArrayList<>();
	            List<Long> idsDePermisos = mapaRelaciones.get(usuario.getIdRol());
	            
	            if (idsDePermisos != null) {
	                for (Long idPermiso : idsDePermisos) {
	                    if (mapaDePermisos.containsKey(idPermiso)) {
	                         nombresDePermisos.add(mapaDePermisos.get(idPermiso));
	                    }
	                }
	            }
	            dto.setPermisos(nombresDePermisos); 
	            resultadoFinal.add(dto);
	        }
	        return resultadoFinal;
	    }		
}