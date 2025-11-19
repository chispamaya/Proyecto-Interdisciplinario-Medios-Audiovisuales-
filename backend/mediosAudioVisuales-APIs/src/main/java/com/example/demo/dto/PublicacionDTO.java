package com.example.demo.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PublicacionDTO {
    private Long id;
    private String tipo; // "CONTENIDO" o "ENCUESTA"
    private Date fechaCreacion; // <--- CRUCIAL PARA EL ORDEN
    
    // Aquí guardaremos el objeto completo (Contenido o EncuestaResultado)
    private Object detalle; 
}