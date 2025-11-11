package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.dto.SegmentoABMDTO;
import com.example.demo.dto.Programa;
import com.example.demo.dto.Segmento;
import com.example.demo.repository.ProgramaRepository;
import com.example.demo.repository.SegmentoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class SegmentoService {
	@Autowired
    private SegmentoRepository segmentoRepository;
	private ProgramaRepository programaRepository;
	
	public String crearSegmento(Segmento nuevoSegmento, Long idUsuarioQueCrea) {
		if (nuevoSegmento.getTitulo() == null || nuevoSegmento.getTitulo().isEmpty()) {
            return "Error: El título no puede estar vacío.";
        }
    	if (nuevoSegmento.getDuracion() <= 0) {
    		return "Error: La duración debe ser mayor a cero.";
    	}
       
        return segmentoRepository.crearSegmento(nuevoSegmento, idUsuarioQueCrea);
    }

    public String borrarSegmento(Long idSegmentoABorrar, Long idUsuarioQueBorra) {
        return segmentoRepository.borrarSegmento(idSegmentoABorrar, idUsuarioQueBorra);
    }
    
  
    public String modificarSegmento(Segmento segmento, Long idUsuarioQueModifica) {
    	if (segmento.getTitulo() == null || segmento.getTitulo().isEmpty()) {
            return "Error: El título no puede estar vacío.";
        }
    	if (segmento.getDuracion() <= 0) {
    		return "Error: La duración debe ser mayor a cero.";
    	}
        return segmentoRepository.modificarSegmento(segmento, idUsuarioQueModifica);
    }
    
    public List<SegmentoABMDTO> listarSegmentosParaABM() {
       
        List<Segmento> segmentos = segmentoRepository.listarTodosLosSegmentos();
        List<Programa> programas = programaRepository.listarTodosLosProgramas();

        Map<Long, String> mapaDeProgramas = programas.stream()
                .collect(Collectors.toMap(Programa::getId, Programa::getNombre));

       
        List<SegmentoABMDTO> resultadoFinal = new ArrayList<>();
        
       
        for (Segmento seg : segmentos) {
       
            SegmentoABMDTO dto = new SegmentoABMDTO();
            
       
            dto.setId(seg.getId());
            dto.setTitulo(seg.getTitulo());
            dto.setDuracion(seg.getDuracion());
            dto.setEstadoAprobacion(seg.getEstadoAprobacion());
            
       
            String nombreProg = mapaDeProgramas.get(seg.getIdPrograma());
            dto.setNombrePrograma(nombreProg);
            
       
            resultadoFinal.add(dto);
        }

        return resultadoFinal;
    }
    
    public Segmento buscarSegmentoPorId(Long id) {
        return segmentoRepository.buscarSegmentoPorId(id);
    }
}
