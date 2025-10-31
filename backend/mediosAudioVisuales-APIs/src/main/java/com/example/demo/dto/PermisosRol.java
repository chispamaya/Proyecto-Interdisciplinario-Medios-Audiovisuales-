package com.example.demo.dto;

import lombok.Data;

@Data
public class PermisosRol {

    // Representa la clave primaria 'id'
    private Long id;
    
    // Representa la clave foránea 'idRol'
    private Long idRol;

    // Representa la clave foránea 'idPermiso'
    private Long idPermiso;
}