package model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "plataforma")
public class Plataforma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String tipo;

    // Una plataforma tiene muchos programas
    @OneToMany(mappedBy = "plataforma")
    private List<Programa> programas;
}