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
import com.example.demo.dto.Auditoria;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.demo.dto.GestionProgramaDTO;
import com.example.demo.dto.Programa;
import com.example.demo.dto.Segmento;
import com.example.demo.repository.ProgramaRepository;
import com.example.demo.repository.SegmentoRepository;
import com.example.demo.repository.AuditoriaRepository;
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
    private SegmentoRepository segmentoRepository;
    
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
    
    public String modificarRolUsuario(Long idUsuarioAModificar, Long idNuevoRol, Long idUsuarioQueModifica) {
        return usuarioRepository.modificarRolDeUsuario(idUsuarioAModificar, idNuevoRol, idUsuarioQueModifica);
    }
    
	public List<GestionProgramaDTO> listarGestionProgramasPorUsuario(Long idUsuario) {
	        
	        // --- LÓGICA DE ORQUESTACIÓN ---
	
	        // Paso 1: Traer todos los programas (para tener los datos)
	        List<Programa> todosLosProgramas = programaRepository.listarTodosLosProgramas();
	
	        // Paso 2: Traer el historial de CREACIÓN de este usuario
	        // (Usa el método que ya tenés en AuditoriaRepository)
	        List<Auditoria> auditorias = auditoriaRepository.buscarAuditoriaPorUsuarioYTTabla(
	            idUsuario, 
	            "programas", // <-- Solo buscamos 'programas'
	            "INSERT"
	        );
	
	        // Paso 3: Crear un "Mapa" (diccionario) de Programas
	        Map<Long, Programa> mapaProgramas = todosLosProgramas.stream()
	                .collect(Collectors.toMap(Programa::getId, programa -> programa));
	
	        // --- LÓGICA DE COMBINACIÓN ---
	        List<GestionProgramaDTO> resultadoFinal = new ArrayList<>();
	
	        // Paso 4: Recorremos la lista de AUDITORÍA (filtrada por este usuario)
	        for (Auditoria aud : auditorias) {
	            
	            Programa programa = mapaProgramas.get(aud.getRegistroAfectadoId());
	
	            if (programa != null) {
	                // B. Creamos la "caja" (DTO)
	                GestionProgramaDTO dto = new GestionProgramaDTO();
	                
	                // 💥 USA LOS NOMBRES CORRECTOS DEL DTO 💥
	                dto.setIdPrograma(programa.getId()); 
	                dto.setTitulo(programa.getNombre());
	                dto.setEstadoAprobacion(programa.getEstadoAprobacion());
	                dto.setFechaCreacion(aud.getFecha()); 
	                
	                // 💥 CÁLCULO DE DURACIÓN 💥
	                if (programa.getHoraInicio() != null && programa.getHoraFin() != null) {
	                    long duracionEnMinutos = Duration.between(programa.getHoraInicio(), programa.getHoraFin()).toMinutes();
	                    dto.setDuracionEnMinutos(duracionEnMinutos); // <-- USA EL NOMBRE CORRECTO
	                } else {
	                    dto.setDuracionEnMinutos(0L); // <-- USA EL NOMBRE CORRECTO
	                }
	                
	                resultadoFinal.add(dto);
	            }
	        }
	
	        // (Ya no hay "Paso 5" para segmentos)
	
	        return resultadoFinal; 
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