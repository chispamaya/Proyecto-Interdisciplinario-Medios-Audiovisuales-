package com.example.demo.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Contenido {
    private Long id;
    private String formato;
    private String rutaArchivo;
    private String texto;
    private Long idUsuario;
    private Date fechaCreacion;
    private List<Long> tags;
    
    // NUEVO: Estado del usuario actual
    private Boolean miReaccion; // true=Like, false=Dislike, null=Nada
}