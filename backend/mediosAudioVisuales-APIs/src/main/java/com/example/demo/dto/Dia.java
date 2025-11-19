package com.example.demo.dto; // (O el paquete que estés usando, ej: com.example.mediosAudioVisuales.model)

import lombok.Data;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
// Se eliminan todas las importaciones de jakarta.persistence.*

@Data // <-- Se queda (Lombok)
public class Dia {

    // Se van @Id y @GeneratedValue
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dia;

    // Se van @ManyToOne y @JoinColumn
    // Reemplazamos el objeto 'Programa' por el ID (la Foreign Key)
    // Tu script SQL la llama 'idPrograma'
    private Long idPrograma;
}