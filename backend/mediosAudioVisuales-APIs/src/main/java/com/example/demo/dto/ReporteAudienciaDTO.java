package com.example.demo.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) para el Reporte de Audiencia.
 * Esto NO es una tabla de la BD, es un "molde" para guardar
 * el resultado de nuestra consulta SQL personalizada (GROUP BY y SUM).
 */
@Data
public class ReporteAudienciaDTO {

    // El ID del posteo (contenido)
    private Long idContenido;

    // El conteo total de 'true' (Likes)
    private Long totalLikes;

    // El conteo total de 'false' (Dislikes)
    private Long totalDislikes;
}