package com.example.demo.dto;

import lombok.Data;
// Ya no se importa jakarta.persistence.*

@Data
public class Segmento {

    private Long id;
    private String estadoAprobacion;
    private Float duracion;
    private String titulo;

    // Convertimos la relación @ManyToOne 'Programa'
    // en un simple campo para el ID (la clave foránea).
    private Long idPrograma;

    // Lombok (@Data) se encarga de los getters/setters/etc.
}