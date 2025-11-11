package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.repository.ProgramaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.AuditoriaRepository;
import com.example.demo.repository.DiaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    

    public List<AprobacionDTO> listarProgramasParaAprobacion() {
        
        List<Programa> programas = programaRepository.listarTodosLosProgramas();
        List<Usuario> usuarios = usuarioRepository.listarTodosLosUsuarios();
        List<Auditoria> auditorias = auditoriaRepository.buscarAuditoriaPorTablaYAccion("programas", "INSERT");

        
        Map<Long, Long> mapaPropuestas = auditorias.stream()
                .collect(Collectors.toMap(
                    Auditoria::getRegistroAfectadoId, // Clave: ID del Programa
                    Auditoria::getUsuarioId,          // Valor: ID del Usuario
                    (idUsuarioExistente, idUsuarioNuevo) -> idUsuarioExistente 
                ));
        
        Map<Long, String> mapaUsuarios = usuarios.stream()
                .collect(Collectors.toMap(Usuario::getId, Usuario::getNombre));

        List<AprobacionDTO> resultadoFinal = new ArrayList<>();


        for (Programa prog : programas) {
            AprobacionDTO dto = new AprobacionDTO();
            
            dto.setIdPrograma(prog.getId());
            dto.setTitulo(prog.getNombre());
            dto.setEstado(prog.getEstadoAprobacion());
            dto.setCategoria(prog.getCategoria());
            

            Long idProponente = mapaPropuestas.get(prog.getId());
            String nombreProponente = mapaUsuarios.get(idProponente);
            
            dto.setPropuestaDe(nombreProponente != null ? nombreProponente : "Desconocido"); 

            resultadoFinal.add(dto);
        }

        return resultadoFinal;
    }
}