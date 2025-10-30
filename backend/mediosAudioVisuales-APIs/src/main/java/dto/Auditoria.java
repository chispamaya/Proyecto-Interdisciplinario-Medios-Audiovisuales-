package dto; // (O el paquete que estés usando, ej: com.example.mediosAudioVisuales.model)

import lombok.Data;
import java.time.LocalDateTime;

@Data // <-- Esta se queda (es de Lombok)
public class Auditoria {

    // Se van @Id y @GeneratedValue
    private Long id;

    private String accion;
    private String tablaM;
    private Integer registro_afectado_id;
    private LocalDateTime fecha;

    // Se van @ManyToOne y @JoinColumn
    // Reemplazamos el objeto 'Usuario' por el ID (la Foreign Key)
    // Tu script SQL la llama 'usuario_id'
    private Long usuarioId; 
}