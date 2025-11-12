package com.example.demo.dto;

import lombok.Data;

@Data
public class AudienciaCon {

    private Long id;
    private Boolean likeOdislike;
    private Long idUsuario;
    private Long idContenido;
}