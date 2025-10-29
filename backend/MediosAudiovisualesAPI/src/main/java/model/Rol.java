package model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String departamento;
    private String permisos;

    // Un rol puede estar asignado a muchos usuarios
    @OneToMany(mappedBy = "rol")
    private List<Usuario> usuarios;
}