package com.example.demo.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PublicacionDTO {
    // Datos comunes para ordenar y filtrar
    private Long id;
    private String tipo; // "CONTENIDO" o "ENCUESTA"
    private Date fechaCreacion;
    
    // Aquí guardaremos el objeto real (puede ser un Contenido o un EncuestaResultado)
    private Object detalle; 
}