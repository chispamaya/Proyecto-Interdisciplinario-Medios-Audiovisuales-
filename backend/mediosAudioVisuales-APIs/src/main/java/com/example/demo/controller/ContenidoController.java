package com.example.demo.controller;

import com.example.demo.dto.Contenido;
import com.example.demo.dto.ContenidoCreacionDTO;
import com.example.demo.service.ContenidoService;
import com.example.demo.service.ContenidoTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenido")
@CrossOrigin(origins = "http://localhost:5173")
public class ContenidoController {

    @Autowired
    private ContenidoService contenidoService;

    @Autowired
    private ContenidoTagService contenidoTagService;

    /**
     * 1. Crear Contenido.
     */
    @PostMapping("/crear")
    public ResponseEntity<String> crearContenidoConTags(@RequestBody ContenidoCreacionDTO request) {
        try {
            if (request.getContenido() == null) {
                return ResponseEntity.badRequest().body("Error: Sin datos.");
            }

            String mensaje = contenidoService.crearContenido(request.getContenido(), request.getIdUsuarioAuditoria());
            
            // (Tags opcional: lógica pendiente si se implementa retorno de ID)
            
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear: " + e.getMessage());
        }
    }

    /**
     * 2. Listar TODO.
     */
    @GetMapping("/todos")
    public ResponseEntity<List<Contenido>> listarTodos() {
        return ResponseEntity.ok(contenidoService.listarTodosLosContenidos());
    }

    /**
     * 3. Listar por Usuario.
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Contenido>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(contenidoService.listarContenidosPorUsuario(idUsuario));
    }

    /**
     * 4. Borrar Contenido.
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
     * 5. Actualizar Tags.
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
}