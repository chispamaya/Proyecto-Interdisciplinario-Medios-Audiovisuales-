package com.example.demo.dto;

import lombok.Data;

@Data
public class contenidoTag {

    // Representa la clave primaria 'id'
    private Long id;
    
    // Representa la clave foránea 'idRol'
    private Long idContenido;

    // Representa la clave foránea 'idPermiso'
    private Long idTag;
}