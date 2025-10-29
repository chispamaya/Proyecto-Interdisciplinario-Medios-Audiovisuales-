package model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "errores")
public class Errores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String mensaje;
    private LocalDateTime fechaYHora;

    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
}