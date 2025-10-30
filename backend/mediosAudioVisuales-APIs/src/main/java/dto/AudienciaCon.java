package dto; // (O el paquete 'dto' si prefieres)

import lombok.Data;

@Data // <-- ¡Esta se queda! Viene de LOMBOK.
public class AudienciaCon {

    // Todas las anotaciones de JPA (@Id, @GeneratedValue) se van.
    // Son solo variables simples.
    private Long id;
    private Boolean likeOdislike;

    // CAMBIO MÁS IMPORTANTE:
    // Ya no hay relaciones mágicas. Mapeamos la columna
    // exacta de tu SQL, que es 'idUsuario'.
    private Long idUsuario;

    // Igual aquí. Mapeamos la columna 'idContenido'.
    private Long idContenido;
}