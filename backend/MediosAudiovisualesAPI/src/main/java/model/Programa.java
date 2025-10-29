package model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "programas")
public class Programa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estadoAprobacion;
    private String categoria;
    private String nombre;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String formatoArchivo;
    private String rutaArchivo;

    // Muchos programas en una plataforma
    @ManyToOne
    @JoinColumn(name = "idPlataforma")
    private Plataforma plataforma;

    // Un programa tiene muchos días
    @OneToMany(mappedBy = "programa")
    private List<Dia> dias;

    // Un programa tiene muchos segmentos
    @OneToMany(mappedBy = "programa")
    private List<Segmento> segmentos;

    // Un programa tiene muchas emisiones
    @OneToMany(mappedBy = "programa")
    private List<Emision> emisiones;
}