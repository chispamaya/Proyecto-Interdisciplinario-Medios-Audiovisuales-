package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AprobacionDTO {
    
   
    private Long idDia; 
    private LocalDate fechaEmision;

    private String tituloPrograma;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    private String propuestaDe; 
    
    private String rutaArchivo;

}