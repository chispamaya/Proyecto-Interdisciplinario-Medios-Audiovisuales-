package com.example.demo.dto; 

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Auditoria {

    private Long id;
    private String accion;
    private String tablaM;
    private LocalDateTime fecha;
    private Long usuarioId; // Correcto (camelCase)

    // 💥 ¡CAMBIO HECHO AQUÍ! 💥
    // Renombrado de 'registro_afectado_id' a 'registroAfectadoId'
    private Long registroAfectadoId; 
}