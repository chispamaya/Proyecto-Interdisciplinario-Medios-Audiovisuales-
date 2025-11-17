package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class ContenidoCreacionDTO {
    // Datos del contenido
    private Contenido contenido;
    
    // Lista de IDs de los tags seleccionados
    private List<Long> listaIdsTags;
    
    // ID del usuario que hace la acción (generalmente vendría del token/sesión)
    private Long idUsuarioAuditoria;
}