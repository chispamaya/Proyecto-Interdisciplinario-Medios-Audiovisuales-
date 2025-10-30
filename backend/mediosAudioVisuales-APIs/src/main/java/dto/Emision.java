package dto; // (O el paquete que estés usando, ej: com.example.mediosAudioVisuales.model)

import lombok.Data;
// Se eliminan todas las importaciones de jakarta.persistence.*

@Data // <-- Se queda (Lombok)
public class Emision {

    // Se van @Id y @GeneratedValue
    private Long id;

    private Boolean enVivo;

    // Se van @ManyToOne y @JoinColumn
    // Se reemplaza el objeto 'Programa' por el ID (la Foreign Key)
    private Long idPrograma;
}