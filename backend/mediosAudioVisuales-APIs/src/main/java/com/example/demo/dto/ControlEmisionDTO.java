package com.example.demo.dto;

import lombok.Data;
import java.time.LocalTime;
import java.util.List;

/**
 * Este es el DTO "Maestro" que tu Service le enviará al Frontend.
 */
@Data
public class ControlEmisionDTO {
    
    // Sección "Programa en Vivo"
    private ProgramaEnVivoInfo enVivo;

    // Sección "Próximos Programas"
    private List<ProgramaInfo> proximos;

    // Sección "Bloque Publicitario"
    private List<SegmentoInfo> publicidades;
    

    // --- 💥 ¡AQUÍ ESTÁ EL CÓDIGO QUE TE FALTA! 💥 ---
    // --- Clases internas para organizar la información ---
    // (Java necesita que estas clases existan)

    /** DTO para la sección "En Vivo" */
    @Data
    public static class ProgramaEnVivoInfo {
        private Long idEmision; 
        private Long idPrograma;
        private String titulo;
        private LocalTime horaInicio;
        private LocalTime horaFin;
    }

    /** DTO para la sección "Próximos" */
    @Data
    public static class ProgramaInfo {
        private Long idPrograma; 

        private String titulo;
        private LocalTime horaInicio;
        private LocalTime horaFin;
    }

    /** DTO para la sección "Bloque Publicitario" */
    @Data
    public static class SegmentoInfo {
        private String titulo;
        private Float duracion;
    }
    // --- 💥 FIN DEL CÓDIGO QUE FALTABA 💥 ---
}