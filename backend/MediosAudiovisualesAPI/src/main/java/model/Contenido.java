package model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "contenidos")
public class Contenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String formato;
    private String rutaArchivo;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    
    // Relación ManyToMany con Tags
    @ManyToMany
    @JoinTable(
        name = "contenido_tag",
        joinColumns = @JoinColumn(name = "idContenido"),
        inverseJoinColumns = @JoinColumn(name = "idTag")
    )
    private Set<Tag> tags;
}