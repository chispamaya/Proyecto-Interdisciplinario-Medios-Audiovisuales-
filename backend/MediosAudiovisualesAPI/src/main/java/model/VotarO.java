package model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "votar_o")
public class VotarO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idOpcion")
    private OpcionE opcion;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
}