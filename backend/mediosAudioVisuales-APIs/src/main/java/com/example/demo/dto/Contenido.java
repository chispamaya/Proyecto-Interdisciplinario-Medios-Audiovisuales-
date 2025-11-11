package com.example.demo.dto;

import lombok.Data;

@Data
public class Contenido {

    private Long id;
    private String formato;
    private String rutaArchivo;
    private Long idUsuario; // ID del usuario que lo subió
    
    // --- Campos Nuevos (Añadidos) ---
    private String titulo;
    private String tipo;        // 'programa', 'audio', 'informe'
    private String estado;      // 'En Revisión', 'Aprobado', 'Rechazado'
    private String duracion;    // "12:34"
    private String tamano;      // "50 MB"
}