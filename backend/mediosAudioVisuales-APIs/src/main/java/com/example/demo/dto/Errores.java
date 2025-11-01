package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Errores {

    // Simplemente campos simples. 
    // Ya no hay @Id ni @GeneratedValue.
    private Long id;

    private String tipo;
    private String mensaje;
    private LocalDateTime fechaYHora;

    // Esta es la diferencia clave.
    // Sin @ManyToOne, no puedes tener un objeto 'Usuario'.
    // En su lugar, almacenas la clave foránea (el ID) directamente.
    private Long idUsuario;
}