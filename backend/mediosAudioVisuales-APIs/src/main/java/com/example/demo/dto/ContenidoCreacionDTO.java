package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class ContenidoCreacionDTO {
    // Datos del contenido (formato, ruta, texto, idUsuario)
    private Contenido contenido;
    
    // Lista de IDs de los tags que se van a asignar (opcional)
    private List<Long> listaIdsTags;
    
    // ID del usuario que realiza la acción (para la auditoría)
    private Long idUsuarioAuditoria;
}