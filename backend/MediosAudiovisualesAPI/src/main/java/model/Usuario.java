package model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String nombre;
    private String contrasenia;

    // Muchos usuarios pueden tener un rol
    @ManyToOne
    @JoinColumn(name = "idRol")
    private Rol rol;
}