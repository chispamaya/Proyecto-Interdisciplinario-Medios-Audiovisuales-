package com.example.demo.dto;

import lombok.Data;

@Data
public class TagReporteDTO {
    // Los nombres deben coincidir con los alias de la consulta SQL (tag, likes, dislikes)
    private String tag;
    private Long likes;
    private Long dislikes;
    
    // Constructor vacío necesario para BeanPropertyRowMapper
    public TagReporteDTO() {}

    public TagReporteDTO(String tag, Long likes, Long dislikes) {
        this.tag = tag;
        this.likes = likes;
        this.dislikes = dislikes;
    }
}