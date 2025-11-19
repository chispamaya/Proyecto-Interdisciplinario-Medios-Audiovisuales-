package com.example.demo.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Contenido {

    private Long id;
    private String formato;
    private String rutaArchivo;
    private String texto; // Campo que SÍ existe en tu tabla 'contenidos'
    private Long idUsuario;
    private Date fechaCreacion;
}