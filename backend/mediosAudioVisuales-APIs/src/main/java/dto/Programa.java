package dto;

import lombok.Data;
import java.time.LocalTime;
import java.util.List;

@Data
public class Programa {
    
    // --- Campos de la tabla 'programas' ---
    
    private Long id;
    private String estadoAprobacion;
    private String categoria;
    private String nombre;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String formatoArchivo;
    private String rutaArchivo;
   
    // Sugerencia: Renombrar para claridad.
    // Esto es correcto para JDBC, es la FK a la tabla 'plataformas'.
    private long idPlataforma; 
  
    // --- Campos de Relaciones (se llenan manualmente) ---
   
    // Esta lista se llenará con una consulta separada
    // (Ej: SELECT * FROM dias_x_programa WHERE id_programa = ?)
    private List<Dia> dias;
   
    // Esta lista se llenará con una consulta separada
    private List<Segmento> segmentos;
   
    // Esta lista se llenará con una consulta separada
    private List<Emision> emisiones;
}