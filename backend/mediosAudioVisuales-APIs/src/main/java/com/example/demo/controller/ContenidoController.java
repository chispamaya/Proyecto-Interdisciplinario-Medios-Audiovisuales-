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

    // 1. Crear Contenido (SubidaMultimedia.jsx)
    @PostMapping("/crear")
    public ResponseEntity<String> crearContenidoConTags(@RequestBody ContenidoCreacionDTO request) {
        try {
            // CORREGIDO: Usamos crearContenido
            String mensaje = contenidoService.crearContenido(request.getContenido(), request.getIdUsuarioAuditoria());
            
            // Nota: Si tuviéramos el ID, aquí llamaríamos a contenidoTagService.actualizarTagsParaContenido
            
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // 2. Borrar Contenido
    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarContenido(@PathVariable Long id, @RequestParam Long idUsuarioAuditoria) {
        try {
            // Limpiamos tags primero
            contenidoTagService.eliminarTagsDeContenido(id, idUsuarioAuditoria);
            // CORREGIDO: Usamos borrarContenido
            String mensaje = contenidoService.borrarContenido(id, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Listar por Usuario (GestionMultimedia.jsx)
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Contenido>> listarPorUsuario(@PathVariable Long idUsuario) {
        // CORREGIDO: Usamos listarContenidosPorUsuario
        return ResponseEntity.ok(contenidoService.listarContenidosPorUsuario(idUsuario));
    }

    // 4. Listar por Usuario y Tipo
    @GetMapping("/usuario/{idUsuario}/tipo/{tipo}")
    public ResponseEntity<List<Contenido>> listarPorUsuarioYTipo(@PathVariable Long idUsuario, @PathVariable String tipo) {
        // CORREGIDO: Usamos listarContenidosPorUsuarioYTipo
        return ResponseEntity.ok(contenidoService.listarContenidosPorUsuarioYTipo(idUsuario, tipo));
    }

    // 5. Listar Todos (EstadoAprobacion.jsx)
    @GetMapping("/todos")
    public ResponseEntity<List<Contenido>> listarTodos() {
        // CORREGIDO: Usamos listarTodosLosContenidos
        return ResponseEntity.ok(contenidoService.listarTodosLosContenidos());
    }

    // 6. Cambiar Estado (Aprobar/Rechazar)
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado, @RequestParam Long idUsuarioAuditoria) {
        try {
            // CORREGIDO: Usamos modificarEstadoContenido
            String mensaje = contenidoService.modificarEstadoContenido(id, nuevoEstado, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 7. Actualizar Tags
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