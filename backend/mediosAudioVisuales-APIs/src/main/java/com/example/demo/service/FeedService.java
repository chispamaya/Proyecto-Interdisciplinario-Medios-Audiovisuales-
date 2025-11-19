package com.example.demo.service;

import com.example.demo.dto.Contenido;
import com.example.demo.dto.EncuestaResultado;
import com.example.demo.dto.PublicacionDTO;
import com.example.demo.repository.ContenidoRepository;
import com.example.demo.repository.EncuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
// Se eliminó el import java.util.Collections que no se usaba

@Service
public class FeedService {

    @Autowired
    private ContenidoRepository contenidoRepository;

    @Autowired
    private EncuestaRepository encuestaRepository; 

    public List<PublicacionDTO> obtenerFeedUnificado() {
        List<PublicacionDTO> feed = new ArrayList<>();

        // 1. TRAER Y CONVERTIR CONTENIDOS
        List<Contenido> contenidos = contenidoRepository.listarTodosLosContenidos();
        for (Contenido c : contenidos) {
            PublicacionDTO item = new PublicacionDTO();
            item.setId(c.getId());
            item.setTipo("CONTENIDO");
            item.setFechaCreacion(c.getFechaCreacion());
            item.setDetalle(c); 
            feed.add(item);
        }

        // 2. TRAER Y CONVERTIR ENCUESTAS
        // ¡Ahora sí llamamos al método que acabamos de crear!
        List<EncuestaResultado> encuestas = encuestaRepository.listarTodasLasEncuestas();

        for (EncuestaResultado e : encuestas) {
            PublicacionDTO item = new PublicacionDTO();
            item.setId(e.getIdEncuesta());
            item.setTipo("ENCUESTA");
            item.setFechaCreacion(e.getFechaCreacion());
            item.setDetalle(e);
            feed.add(item);
        }

        // 3. ORDENAR LA LISTA MEZCLADA (Lo más nuevo primero)
        feed.sort((p1, p2) -> {
            // Manejo de nulos para evitar errores si falta alguna fecha
            if (p1.getFechaCreacion() == null && p2.getFechaCreacion() == null) return 0;
            if (p1.getFechaCreacion() == null) return 1; // Nulos al final
            if (p2.getFechaCreacion() == null) return -1;
            
            return p2.getFechaCreacion().compareTo(p1.getFechaCreacion());
        });

        return feed;
    }
}