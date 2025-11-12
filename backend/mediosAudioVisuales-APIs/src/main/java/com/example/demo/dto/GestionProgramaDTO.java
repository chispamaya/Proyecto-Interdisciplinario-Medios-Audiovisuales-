package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GestionProgramaDTO {
    
	private Long idPrograma; // Para los botones de Editar/Borrar
    private String titulo;
    private String estadoAprobacion;
    private LocalDateTime fechaCreacion; 
    
    // La duración calculada (en minutos)
    private Long duracionEnMinutos;
}