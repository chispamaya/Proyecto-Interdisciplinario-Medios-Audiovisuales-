package com.example.demo.dto;

import lombok.Data;

@Data
public class SegmentoABMDTO {
    
    private Long id;
    private String titulo;
    private Float duracion;
    private String estadoAprobacion;
    
    private String nombrePrograma; 
}