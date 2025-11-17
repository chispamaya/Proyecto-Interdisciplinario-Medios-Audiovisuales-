package com.example.demo.dto;

import lombok.Data;
// Ya no se importa jakarta.persistence.*

@Data
public class VotarO {

    private Long id;

    // Convertimos la relación @ManyToOne 'OpcionE'
    // en un simple campo para el ID (la clave foránea).
    private Long idOpcion;

    // Convertimos la relación @ManyToOne 'Usuario'
    // en un simple campo para el ID (la clave foránea).
    private Long idUsuario;
 // ID de la encuesta a la que pertenece el voto
    // (Este es el campo que faltaba y que el controlador necesita)
    private Long idEncuesta;
}