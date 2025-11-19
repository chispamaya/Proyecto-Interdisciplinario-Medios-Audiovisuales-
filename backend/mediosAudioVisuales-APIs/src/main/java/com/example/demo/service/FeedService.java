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

    // Ahora recibimos el ID del usuario (puede ser null si es un invitado)
    public List<PublicacionDTO> obtenerFeedUnificado(Long idUsuario) {
        List<PublicacionDTO> feed = new ArrayList<>();

        // 1. Obtener y procesar CONTENIDOS
        List<Contenido> contenidos = contenidoRepository.listarTodosLosContenidos();
        
        for (Contenido c : contenidos) {
            // A. Cargar Tags
            c.setTags(contenidoRepository.obtenerIdsTagsPorContenido(c.getId()));
            
            // B. Cargar Reacción del usuario (Si está logueado)
            if (idUsuario != null) {
                // Busca en la tabla 'audiencia_con' si hay like/dislike
                Boolean reaccion = contenidoRepository.obtenerReaccionUsuario(c.getId(), idUsuario);
                c.setMiReaccion(reaccion);
            }

            PublicacionDTO item = new PublicacionDTO();
            item.setId(c.getId());
            item.setTipo("CONTENIDO");
            item.setFechaCreacion(c.getFechaCreacion());
            item.setDetalle(c); 
            feed.add(item);
        }

        // 2. Obtener y procesar ENCUESTAS
        List<EncuestaResultado> encuestas = encuestaRepository.listarTodasLasEncuestas();
        
        for (EncuestaResultado e : encuestas) {
            // Verificar si el usuario votó ESTA opción específica
            if (idUsuario != null) {
                Long idOpcionVotada = encuestaRepository.obtenerOpcionVotadaPorUsuario(e.getIdEncuesta(), idUsuario);
                
                // Si el ID de la opción votada coincide con esta opción, marcamos true
                if (idOpcionVotada != null && idOpcionVotada.equals(e.getIdOpcion())) {
                    e.setVotadaPorMi(true);
                } else {
                    e.setVotadaPorMi(false);
                }
            }

            PublicacionDTO item = new PublicacionDTO();
            item.setId(e.getIdEncuesta());
            item.setTipo("ENCUESTA");
            item.setFechaCreacion(e.getFechaCreacion());
            item.setDetalle(e);
            feed.add(item);
        }

        // 3. Ordenar por fecha descendente
        feed.sort((p1, p2) -> {
            if (p1.getFechaCreacion() == null) return 1;
            if (p2.getFechaCreacion() == null) return -1;
            return p2.getFechaCreacion().compareTo(p1.getFechaCreacion());
        });

        return feed;
    }
}