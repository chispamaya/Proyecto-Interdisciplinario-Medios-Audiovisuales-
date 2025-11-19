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

@Service
public class FeedService {

    @Autowired
    private ContenidoRepository contenidoRepository;

    @Autowired
    private EncuestaRepository encuestaRepository;

    public List<PublicacionDTO> obtenerFeedUnificado() {
        List<PublicacionDTO> feed = new ArrayList<>();

        // 1. Obtener y convertir CONTENIDOS
        List<Contenido> contenidos = contenidoRepository.listarTodosLosContenidos();
        for (Contenido c : contenidos) {
            PublicacionDTO item = new PublicacionDTO();
            item.setId(c.getId());
            item.setTipo("CONTENIDO");
            item.setFechaCreacion(c.getFechaCreacion());
            item.setDetalle(c); // Guardamos todo el objeto contenido
            feed.add(item);
        }

        // 2. Obtener y convertir ENCUESTAS
        List<EncuestaResultado> encuestas = encuestaRepository.listarTodasLasEncuestas();
        for (EncuestaResultado e : encuestas) {
            PublicacionDTO item = new PublicacionDTO();
            item.setId(e.getIdEncuesta());
            item.setTipo("ENCUESTA");
            item.setFechaCreacion(e.getFechaCreacion());
            item.setDetalle(e); // Guardamos todo el resultado de la encuesta
            feed.add(item);
        }

        // 3. ORDENAR la lista combinada por fecha (DESCENDENTE)
        feed.sort((p1, p2) -> {
            // Si alguna fecha es nula, la mandamos al final para que no rompa
            if (p1.getFechaCreacion() == null && p2.getFechaCreacion() == null) return 0;
            if (p1.getFechaCreacion() == null) return 1;
            if (p2.getFechaCreacion() == null) return -1;
            
            // Orden descendente (más nuevo arriba)
            return p2.getFechaCreacion().compareTo(p1.getFechaCreacion());
        });

        return feed;
    }
}