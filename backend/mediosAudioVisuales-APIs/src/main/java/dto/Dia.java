package dto; // (O el paquete que estés usando, ej: com.example.mediosAudioVisuales.model)

import lombok.Data;
import java.time.LocalDate;
// Se eliminan todas las importaciones de jakarta.persistence.*

@Data // <-- Se queda (Lombok)
public class Dia {

    // Se van @Id y @GeneratedValue
    private Long id;

    private LocalDate dia;

    // Se van @ManyToOne y @JoinColumn
    // Reemplazamos el objeto 'Programa' por el ID (la Foreign Key)
    // Tu script SQL la llama 'idPrograma'
    private Long idPrograma;
}