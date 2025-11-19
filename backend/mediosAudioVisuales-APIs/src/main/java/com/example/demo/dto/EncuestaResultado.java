package com.example.demo.dto;

import java.util.Date;
import lombok.Data;

@Data
public class EncuestaResultado {
    private Long idEncuesta;
    private String preguntar;
    private Long idCreador;
    private Long idOpcion;
    private String opcion;
    private Long totalVotos;
    private Date fechaCreacion;

    // NUEVO: Si el usuario votó esta opción específica
    private boolean votadaPorMi; 
}