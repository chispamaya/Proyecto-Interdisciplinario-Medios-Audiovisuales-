package model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "audiencia_con")
public class AudienciaCon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean likeOdislike;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "idContenido")
    private Contenido contenido;
}