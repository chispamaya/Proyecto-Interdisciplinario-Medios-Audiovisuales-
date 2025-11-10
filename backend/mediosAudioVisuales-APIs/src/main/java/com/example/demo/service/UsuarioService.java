package com.example.demo.service;
import com.example.demo.dto.PerfilDTO;
import com.example.demo.dto.Rol;
import com.example.demo.dto.Usuario;

import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.RolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;
    
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
	}