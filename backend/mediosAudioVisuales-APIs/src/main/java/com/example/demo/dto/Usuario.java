package com.example.demo.dto;

import lombok.Data;
// Ya no se importa jakarta.persistence.*

@Data
public class Usuario {

    private Long id;
    private String email; // La restricción 'unique' es de la DB, no del POJO
    private String nombre;
    private String contrasenia;

    // Convertimos la relación @ManyToOne 'Rol'
    // en un simple campo para el ID (la clave foránea).
    private Long idRol;
}