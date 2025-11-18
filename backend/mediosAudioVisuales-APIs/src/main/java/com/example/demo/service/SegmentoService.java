package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.SegmentoABMDTO;
import com.example.demo.dto.Segmento;
import com.example.demo.repository.SegmentoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SegmentoService {
	@Autowired
    private SegmentoRepository segmentoRepository;
	
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
        
        // ✅ SIMPLIFICACIÓN: Llama directamente al Repository, que ya trae el DTO completo
        return segmentoRepository.listarSegmentosParaABM();
    }
    
    public Segmento buscarSegmentoPorId(Long id) {
        return segmentoRepository.buscarSegmentoPorId(id);
    }
}
