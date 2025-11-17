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
     * Crear Contenido.
     * Recibe ContenidoCreacionDTO (contenido + listaIdsTags + idUsuarioAuditoria).
     */
    @PostMapping("/crear")
    public ResponseEntity<String> crearContenidoConTags(@RequestBody ContenidoCreacionDTO request) {
        try {
            // 1. Validamos que el objeto contenido no sea nulo
            if (request.getContenido() == null) {
                return ResponseEntity.badRequest().body("Error: No se enviaron datos del contenido.");
            }

            // 2. Llamamos al servicio para crear el contenido
            String mensaje = contenidoService.crearContenido(request.getContenido(), request.getIdUsuarioAuditoria());
            
            // NOTA: Si en el futuro implementamos que crearContenido retorne el ID,
            // aquí llamaríamos a contenidoTagService.actualizarTagsParaContenido(...)
            
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear contenido: " + e.getMessage());
        }
    }

    /**
     * Listar TODO el contenido (para EstadoAprobacion.jsx).
     */
    @GetMapping("/todos")
    public ResponseEntity<List<Contenido>> listarTodos() {
        return ResponseEntity.ok(contenidoService.listarTodosLosContenidos());
    }

    /**
     * Listar contenido de un usuario específico (para GestionMultimedia.jsx).
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Contenido>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(contenidoService.listarContenidosPorUsuario(idUsuario));
    }

    /**
     * Listar por usuario y tipo (filtro de GestionMultimedia.jsx).
     */
    @GetMapping("/usuario/{idUsuario}/tipo/{tipo}")
    public ResponseEntity<List<Contenido>> listarPorUsuarioYTipo(@PathVariable Long idUsuario, @PathVariable String tipo) {
        return ResponseEntity.ok(contenidoService.listarContenidosPorUsuarioYTipo(idUsuario, tipo));
    }

    /**
     * Borrar un contenido por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarContenido(@PathVariable Long id, @RequestParam Long idUsuarioAuditoria) {
        try {
            // Primero intentamos borrar los tags asociados para mantener la integridad
            contenidoTagService.eliminarTagsDeContenido(id, idUsuarioAuditoria);
            
            // Luego borramos el contenido
            String mensaje = contenidoService.borrarContenido(id, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Cambiar el estado de aprobación (Aprobado/Rechazado).
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado, @RequestParam Long idUsuarioAuditoria) {
        try {
            String mensaje = contenidoService.modificarEstadoContenido(id, nuevoEstado, idUsuarioAuditoria);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Actualizar los tags de un contenido existente.
     */
    @PutMapping("/{id}/tags")
    public ResponseEntity<String> actualizarTags(@PathVariable Long id, @RequestBody List<Long> nuevosTags, @RequestParam Long idUsuarioAuditoria) {
        try {
            contenidoTagService.actualizarTagsParaContenido(id, nuevosTags, idUsuarioAuditoria);
            return ResponseEntity.ok("Tags actualizados correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}