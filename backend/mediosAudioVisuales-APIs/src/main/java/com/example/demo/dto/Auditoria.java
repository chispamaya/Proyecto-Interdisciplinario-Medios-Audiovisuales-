package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Auditoria {

    private Long id;
    private String accion;
    private Long usuarioId; // Mapeado desde 'usuario_id'
    private String tablaM;
    private int registro_afectado_id;
    private LocalDateTime fecha;
}