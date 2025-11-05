package com.example.demo.dto;

import lombok.Data;

/**
 * DTO (Molde) que representa UNA FILA del resultado
 * que devuelve tu SP s('encuesta', id, @m).
 * Es un "molde a medida" para el JOIN de 3 tablas que hace tu SP.
 */
@Data
public class EncuestaResultado {

    // Info de la Encuesta (e)
    private Long idEncuesta;
    private String preguntar;
    private Long idCreador;

    // Info de la Opción (o)
    private Long idOpcion;
    private String opcion;

    // Info de los Votos (v)
    private Long totalVotos;
}