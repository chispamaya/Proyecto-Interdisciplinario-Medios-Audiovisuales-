package model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "emisiones")
public class Emision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean enVivo;

    @ManyToOne
    @JoinColumn(name = "idPrograma")
    private Programa programa;
}