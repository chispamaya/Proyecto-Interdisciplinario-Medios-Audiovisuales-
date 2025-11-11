package com.example.demo.dto;

import lombok.Data;

@Data
public class AprobacionDTO {

    private Long idPrograma; 
    
    private String titulo;
    private String estado;
    private String categoria;
    
    private String propuestaDe; 
}