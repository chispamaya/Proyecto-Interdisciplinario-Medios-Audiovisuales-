package model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "encuesta")
public class Encuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String preguntar;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    // Una encuesta tiene muchas opciones
    @OneToMany(mappedBy = "encuesta")
    private List<OpcionE> opciones;
}