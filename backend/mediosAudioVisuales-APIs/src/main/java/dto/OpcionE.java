package dto;

import lombok.Data;

@Data
public class OpcionE {

    private Long id;
    private String opcion;

    // Igual que antes:
    // Sin JPA, no mapeamos el objeto 'Encuesta' completo.
    // Simplemente guardamos la clave foránea (Foreign Key) como un Long.
    private Long idEncuesta;
    
    // Lombok (@Data) se encarga de los getters/setters/etc.
}