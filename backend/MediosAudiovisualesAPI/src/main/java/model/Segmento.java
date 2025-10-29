package model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "segmentos")
public class Segmento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estadoAprobacion;
    private Float duracion;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "idPrograma")
    private Programa programa;
}