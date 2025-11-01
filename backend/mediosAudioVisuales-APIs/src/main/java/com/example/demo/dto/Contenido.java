package com.example.demo.dto; // (O el paquete que estés usando, ej: com.example.mediosAudioVisuales.model)

import lombok.Data;
// Se eliminan todas las importaciones de jakarta.persistence.*
// Se elimina la importación de java.util.Set

@Data // <-- Se queda (Lombok)
public class Contenido {

    // Se van @Id y @GeneratedValue
    private Long id;

    private String formato;
    private String rutaArchivo;

    // Se van @ManyToOne y @JoinColumn
    // Se reemplaza el objeto 'Usuario' por el ID (la Foreign Key)
    private Long idUsuario;

    // ¡CAMBIO IMPORTANTE!
    // El 'Set<Tag> tags' se elimina por completo.
}