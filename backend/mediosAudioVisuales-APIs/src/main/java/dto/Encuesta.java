package dto; // (O el paquete que estés usando, ej: com.example.mediosAudioVisuales.model)

import lombok.Data;
// Se eliminan todas las importaciones de jakarta.persistence.*
// Se elimina la importación de java.util.List

@Data // <-- Se queda (Lombok)
public class Encuesta {

    // Se van @Id y @GeneratedValue
    private Long id;

    private String preguntar;

    // Se van @ManyToOne y @JoinColumn
    // Se reemplaza el objeto 'Usuario' por el ID (la Foreign Key)
    private Long idUsuario;

    // Se va @OneToMany
    // Se elimina la lista 'private List<OpcionE> opciones;'
}