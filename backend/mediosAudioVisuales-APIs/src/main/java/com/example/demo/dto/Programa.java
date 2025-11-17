package com.example.demo.dto;

import lombok.Data;
import java.time.LocalTime;
import java.util.List;

@Data
public class Programa {
    
    // --- Campos de la tabla 'programas' ---
    
    private Long id;
    private String estadoAprobacion;
    private String categoria;
    private String nombre;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String formatoArchivo;
    private String rutaArchivo;
    
    // --- 💥 ¡CAMPOS FALTANTES AGREGADOS! 💥 ---
    private String formatoInforme;
    private String rutaInforme;
    // --- 💥 ---
    
    private long idPlataforma; 
 
    // --- Campos de Relaciones (se llenan en el Service) ---
    private List<Dia> dias;
    private List<Segmento> segmentos;
    private List<Emision> emisiones;
}