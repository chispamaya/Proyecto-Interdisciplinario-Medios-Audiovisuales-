package model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "opcion_e")
public class OpcionE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String opcion;

    @ManyToOne
    @JoinColumn(name = "idEncuesta")
    private Encuesta encuesta;
}