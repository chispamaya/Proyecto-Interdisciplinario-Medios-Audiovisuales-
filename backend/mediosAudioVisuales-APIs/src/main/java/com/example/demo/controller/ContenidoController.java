package com.example.demo.controller;

import com.example.demo.dto.Contenido;
import com.example.demo.dto.ContenidoCreacionDTO;
import com.example.demo.service.ContenidoService;
import com.example.demo.service.ContenidoTagService;
import com.example.demo.service.TagService;
import com.example.demo.repository.ContenidoRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map; 
import java.util.UUID;

@RestController
@RequestMapping("/api/contenido")
@CrossOrigin(origins = "http://localhost:5173") // Refuerzo de CORS
public class ContenidoController {

    @Autowired
    private ContenidoService contenidoService;

    @Autowired
    private ContenidoTagService contenidoTagService;
    
    @Autowired
    private ContenidoRepository contenidoRepository; 
    
    @Autowired
    private TagService tagService;

    // --- ENDPOINT SUBIDA DE IMAGEN ---
    @PostMapping("/upload")
    public ResponseEntity<String> subirArchivo(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío.");
            }

            // -----------------------------------------------------------
            // 📍 AQUÍ CAMBIAS LA RUTA DE GUARDADO
            // Si quieres una ruta absoluta (ej: C:/imagenes/), ponla aquí.
            // "uploads/imagenes/" crea la carpeta dentro del proyecto.
            String uploadDir = "uploads/imagenes/"; 
            // -----------------------------------------------------------

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            
            String nuevoNombre = UUID.randomUUID().toString() + extension;
            Path filePath = uploadPath.resolve(nuevoNombre);
            
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Ruta web para el frontend (debe coincidir con WebConfig)
            String rutaWeb = "/uploads/imagenes/" + nuevoNombre;
            return ResponseEntity.ok(rutaWeb);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al subir archivo: " + e.getMessage());
        }
    }

    // --- RESTO DE MÉTODOS (IGUALES QUE ANTES) ---
    
    @PostMapping("/crear")
    public ResponseEntity<String> crearContenidoConTags(@RequestBody ContenidoCreacionDTO request) {
        try {
            if (request.getContenido() == null) return ResponseEntity.badRequest().body("Error: Sin datos.");
            
            String mensaje = contenidoService.crearContenido(request.getContenido(), request.getIdUsuarioAuditoria());
            
            List<Contenido> contenidosUser = contenidoService.listarContenidosPorUsuario(request.getContenido().getIdUsuario());
            if (contenidosUser.isEmpty()) return ResponseEntity.ok(mensaje);
            
            Long idContenidoNuevo = contenidosUser.stream().mapToLong(Contenido::getId).max().orElseThrow();
            List<Long> idsTags = tagService.resolverIdsDeTags(request.getTagsTexto(), request.getIdUsuarioAuditoria());
            contenidoTagService.actualizarTagsParaContenido(idContenidoNuevo, idsTags, request.getIdUsuarioAuditoria());

            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

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

    @GetMapping("/todos")
    public ResponseEntity<List<Contenido>> listarTodos() {
        return ResponseEntity.ok(contenidoService.listarTodosLosContenidos());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Contenido>> listarPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(contenidoService.listarContenidosPorUsuario(idUsuario));
    }

    @PutMapping("/{id}/tags")
    public ResponseEntity<String> actualizarTags(@PathVariable Long id, @RequestBody List<Long> nuevosTags, @RequestParam Long idUsuarioAuditoria) {
        try {
            contenidoTagService.actualizarTagsParaContenido(id, nuevosTags, idUsuarioAuditoria);
            return ResponseEntity.ok("Tags actualizados.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/valorar")
    public ResponseEntity<String> valorarContenido(@RequestBody Map<String, Object> payload, @RequestParam Long idUsuarioAuditoria) {
        try {
            Integer idC = (Integer) payload.get("idContenido");
            Integer idU = (Integer) payload.get("idUsuario");
            Boolean esLike = (Boolean) payload.get("esLike");
            return ResponseEntity.ok(contenidoRepository.valorarContenido(esLike, Long.valueOf(idC), Long.valueOf(idU), idUsuarioAuditoria));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/borrarValoracion")
    public ResponseEntity<String> borrarValoracion(@RequestBody Map<String, Object> payload, @RequestParam Long idUsuarioAuditoria) {
        try {
            Integer idC = (Integer) payload.get("idContenido");
            Integer idU = (Integer) payload.get("idUsuario");
            return ResponseEntity.ok(contenidoRepository.borrarValoracion(Long.valueOf(idC), Long.valueOf(idU), idUsuarioAuditoria));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}