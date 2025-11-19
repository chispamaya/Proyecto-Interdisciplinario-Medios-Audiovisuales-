package com.example.demo.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class ParrillaDTO {
    private String nombrePrograma;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    // Opcional: podrías agregar color, categoría, etc.
}