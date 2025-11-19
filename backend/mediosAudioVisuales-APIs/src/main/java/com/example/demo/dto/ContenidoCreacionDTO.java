package com.example.demo.dto;

import java.util.List;
import lombok.Data;

@Data
public class ContenidoCreacionDTO {
    private Contenido contenido;
    
    // CAMBIO: Ahora recibimos nombres, no IDs
    private List<String> tagsTexto; 
    
    private Long idUsuarioAuditoria;
}