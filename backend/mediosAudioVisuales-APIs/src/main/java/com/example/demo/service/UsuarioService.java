package com.example.demo.service;

// Importamos todas las "cajas" (DTOs) y "brazos" (Repositories) que necesita
import com.example.demo.dto.EmpleadoDto;
import com.example.demo.dto.PerfilDTO;
import com.example.demo.dto.Rol;
import com.example.demo.dto.Usuario;
import com.example.demo.dto.Permisos;
import com.example.demo.dto.PermisosRol;
import com.example.demo.repository.PermisosRepository;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PermisosRepository permisosRepository;
    
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
    
    public String modificarRolUsuario(Long idUsuarioAModificar, Long idNuevoRol, Long idUsuarioQueModifica) {
        return usuarioRepository.modificarRolDeUsuario(idUsuarioAModificar, idNuevoRol, idUsuarioQueModifica);
    }
    
    
	public PerfilDTO obtenerDatosPerfil(Long idUsuario) {
	        
	        Usuario usuario = usuarioRepository.buscarUsuarioPorId(idUsuario);
	
	        if (usuario == null) {
	            return null; 
	        }

	        Rol rol = rolRepository.buscarRolPorId(usuario.getIdRol());
	
	        if (rol == null) {
	            return null; 
	        }
	
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