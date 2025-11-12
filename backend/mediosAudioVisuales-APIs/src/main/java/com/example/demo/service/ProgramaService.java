package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.repository.ProgramaRepository;
import com.example.demo.repository.SegmentoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.AuditoriaRepository;
import com.example.demo.repository.DiaRepository;
import com.example.demo.repository.EmisionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.GestionProgramaDTO;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//1. IMPORTA TODO LO NUEVO

import java.time.LocalDate; // <-- Para la fecha de hoy
import java.time.LocalTime; // <-- Para la hora actual

@Service
public class ProgramaService {

  
    @Autowired
    private ProgramaRepository programaRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository; 

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DiaRepository diaRepository; 
    @Autowired private EmisionRepository emisionRepository;
    @Autowired private SegmentoRepository segmentoRepository;
    
 
    public String crearPrograma(Programa nuevoPrograma, Long idUsuarioQueCrea) {
        if (nuevoPrograma.getNombre() == null || nuevoPrograma.getNombre().isEmpty()) {
            return "Error: El programa debe tener un nombre.";
        }
        return programaRepository.crearPrograma(nuevoPrograma, idUsuarioQueCrea);
    }

 
    public String modificarPrograma(Programa programa, Long idUsuarioQueModifica) {
        if (programa.getNombre() == null || programa.getNombre().isEmpty()) {
            return "Error: El programa debe tener un nombre.";
        }
        return programaRepository.modificarPrograma(programa, idUsuarioQueModifica);
    }
    
 
    public String borrarPrograma(Long idProgramaABorrar, Long idUsuarioQueBorra) {
        return programaRepository.borrarPrograma(idProgramaABorrar, idUsuarioQueBorra);
    }

  
    public Programa buscarProgramaPorId(Long id) {
        return programaRepository.buscarProgramaPorId(id);
    }

 
    public List<Programa> listarTodosLosProgramas() {
        return programaRepository.listarTodosLosProgramas();
    }


  
    public String asignarDia(Dia dia, Long idUsuarioQueAsigna) {
        return diaRepository.crearDia(dia, idUsuarioQueAsigna);
    }


    public String quitarDia(Long idDia, Long idUsuarioQueQuita) {
        return diaRepository.borrarDia(idDia, idUsuarioQueQuita);
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
	public ControlEmisionDTO getControlEmisionDashboard() {
	        
	        // --- LÓGICA DE ORQUESTACIÓN ---
	        ControlEmisionDTO dashboard = new ControlEmisionDTO();
	        LocalDate hoy = LocalDate.now();
	        LocalTime ahora = LocalTime.now();
	
	        

	        Emision emisionEnVivo = emisionRepository.listarTodasLasEmisiones().stream()
	                .filter(Emision::getEnVivo) // Filtra la lista por enVivo == true
	                .findFirst()
	                .orElse(null); // Si no hay ninguna, 'emisionEnVivo' es null
	
	        if (emisionEnVivo != null) {
	            // 2. Si hay algo "En Vivo", buscamos sus datos
	            Programa programaEnVivo = programaRepository.buscarProgramaPorId(emisionEnVivo.getIdPrograma());
	            
	            if (programaEnVivo != null) {
	                // 3. Llenamos el DTO de "En Vivo"
	                ControlEmisionDTO.ProgramaEnVivoInfo enVivoInfo = new ControlEmisionDTO.ProgramaEnVivoInfo();
	                enVivoInfo.setIdEmision(emisionEnVivo.getId());
	                enVivoInfo.setIdPrograma(programaEnVivo.getId());
	                enVivoInfo.setTitulo(programaEnVivo.getNombre());
	                enVivoInfo.setHoraInicio(programaEnVivo.getHoraInicio());
	                enVivoInfo.setHoraFin(programaEnVivo.getHoraFin());
	                dashboard.setEnVivo(enVivoInfo);
	
	                // 4. Llenamos los "Bloques Publicitarios" (Segmentos) de ESE programa
	                List<Segmento> segmentos = segmentoRepository.listarSegmentosPorPrograma(programaEnVivo.getId());
	                
	                // Convertimos la lista de Segmento a SegmentoInfo
	                dashboard.setPublicidades(
	                    segmentos.stream().map(seg -> {
	                        ControlEmisionDTO.SegmentoInfo segInfo = new ControlEmisionDTO.SegmentoInfo();
	                        segInfo.setTitulo(seg.getTitulo());
	                        segInfo.setDuracion(seg.getDuracion());
	                        return segInfo;
	                    }).collect(Collectors.toList())
	                );
	            }
	        }
	        
	        // --- B. DATOS "PRÓXIMOS PROGRAMAS" ---
	        
	        // 1. Buscamos los programas de "HOY"
	        List<Dia> diasDeHoy = diaRepository.listarDiasPorFecha(hoy);
	        List<Programa> todosLosProgramas = programaRepository.listarTodosLosProgramas();
	        
	        // 2. Creamos el "mapa" de programas para buscar rápido
	        Map<Long, Programa> mapaProgramas = todosLosProgramas.stream()
	                .collect(Collectors.toMap(Programa::getId, p -> p));
	
	        // 3. Filtramos y Mapeamos la lista
	        List<ControlEmisionDTO.ProgramaInfo> proximos = new ArrayList<>();
	        for (Dia dia : diasDeHoy) {
	            Programa prog = mapaProgramas.get(dia.getIdPrograma());
	            
	            // Si el programa existe Y su hora de inicio es DESPUÉS de ahora...
	            if (prog != null && prog.getHoraInicio() != null && prog.getHoraInicio().isAfter(ahora)) {
	                ControlEmisionDTO.ProgramaInfo progInfo = new ControlEmisionDTO.ProgramaInfo();
	                progInfo.setTitulo(prog.getNombre());
	                progInfo.setHoraInicio(prog.getHoraInicio());
	                progInfo.setHoraFin(prog.getHoraFin());
	                proximos.add(progInfo);
	            }
	        }
	        
	        // (Opcional: ordenar la lista por hora de inicio)
	        proximos.sort((p1, p2) -> p1.getHoraInicio().compareTo(p2.getHoraInicio()));
	        
	        dashboard.setProximos(proximos);
	        
	        return dashboard;
	    }
}
