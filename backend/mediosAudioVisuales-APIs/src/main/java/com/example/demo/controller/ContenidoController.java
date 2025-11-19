package com.example.demo.controller;

import com.example.demo.dto.Contenido;
import com.example.demo.dto.ContenidoCreacionDTO;
import com.example.demo.service.ContenidoService;
import com.example.demo.service.ContenidoTagService;
import com.example.demo.repository.ContenidoRepository; // Necesario para llamar a los métodos de valorar directamente
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Import necesario para recibir el JSON de valoración

@RestController
@RequestMapping("/api/contenido")
public class ContenidoController {

    @Autowired
    private ContenidoService contenidoService;

    @Autowired
    private ContenidoTagService contenidoTagService;
    
    // Inyectamos el repositorio directamente para las valoraciones (o podrías hacerlo vía servicio)
    @Autowired
    private ContenidoRepository contenidoRepository; 

    /**
     * 1. Crear Contenido
     */
    @PostMapping("/crear")
    public ResponseEntity<String> crearContenidoConTags(@RequestBody ContenidoCreacionDTO request) {
        try {
            if (request.getContenido() == null) {
                return ResponseEntity.badRequest().body("Error: No se enviaron datos del contenido.");
            }
            String mensaje = contenidoService.crearContenido(request.getContenido(), request.getIdUsuarioAuditoria());
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear: " + e.getMessage());
        }
    }

    /**
     * 2. Borrar Contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarContenido(@PathVariable Long id, @RequestParam Long idUsuarioAuditoria) {
        try {
            contenidoTagService.eliminarTagsDeContenido(id, idUsuarioAuditoria);
            String mensaje = contenidoService.borrarContenido(id, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 3. Listar TODOS
     */
    @GetMapping("/todos")
    public ResponseEntity<List<Contenido>> listarTodos() {
        return ResponseEntity.ok(contenidoService.listarTodosLosContenidos());
    }

    /**
     * 4. Listar por usuario
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Contenido>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(contenidoService.listarContenidosPorUsuario(idUsuario));
    }

    /**
     * 5. Actualizar Tags
     */
    @PutMapping("/{id}/tags")
    public ResponseEntity<String> actualizarTags(@PathVariable Long id, @RequestBody List<Long> nuevosTags, @RequestParam Long idUsuarioAuditoria) {
        try {
            contenidoTagService.actualizarTagsParaContenido(id, nuevosTags, idUsuarioAuditoria);
            return ResponseEntity.ok("Tags actualizados.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==================================================================
    // 👇👇👇 ESTOS SON LOS MÉTODOS QUE TE FALTABAN 👇👇👇
    // ==================================================================

    /**
     * 6. Valorar (Dar Like/Dislike) - SP 'ld'
     */
    @PostMapping("/valorar")
    public ResponseEntity<String> valorarContenido(
            @RequestBody Map<String, Object> payload, 
            @RequestParam Long idUsuarioAuditoria) {
        try {
            // Extraemos los datos del JSON
            Integer idContenidoInt = (Integer) payload.get("idContenido");
            Integer idUsuarioInt = (Integer) payload.get("idUsuario");
            Boolean esLike = (Boolean) payload.get("esLike");
            
            // Convertimos a Long
            Long idContenido = Long.valueOf(idContenidoInt);
            Long idUsuario = Long.valueOf(idUsuarioInt);

            String mensaje = contenidoRepository.valorarContenido(esLike, idContenido, idUsuario, idUsuarioAuditoria);
            
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al valorar: " + e.getMessage());
        }
    }

    /**
     * 7. Borrar Valoración (Quitar Like/Dislike) - SP 'bv'
     */
    @PostMapping("/borrarValoracion")
    public ResponseEntity<String> borrarValoracion(
            @RequestBody Map<String, Object> payload, 
            @RequestParam Long idUsuarioAuditoria) {
        try {
            Integer idContenidoInt = (Integer) payload.get("idContenido");
            Integer idUsuarioInt = (Integer) payload.get("idUsuario");
            
            Long idContenido = Long.valueOf(idContenidoInt);
            Long idUsuario = Long.valueOf(idUsuarioInt);

            String mensaje = contenidoRepository.borrarValoracion(idContenido, idUsuario, idUsuarioAuditoria);
            
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al borrar valoración: " + e.getMessage());
        }
    }
}