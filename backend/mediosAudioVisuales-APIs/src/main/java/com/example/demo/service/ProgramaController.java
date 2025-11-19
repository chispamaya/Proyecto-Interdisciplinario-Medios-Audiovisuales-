package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.ProgramaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/programas")
public class ProgramaController {

    @Autowired
    private ProgramaService programaService;

    // Carpeta de almacenamiento
    private final Path fileStorageLocation = Paths.get("archivos_multimedia").toAbsolutePath().normalize();

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("📂 [CONTROLLER] Carpeta lista en: " + this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Error al crear directorio.", ex);
        }
    }

    // --- UPLOAD CON LOGS ---
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("📥 [UPLOAD] Recibiendo: " + file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("   ✅ Guardado en disco como: " + fileName);
            
            return ResponseEntity.ok(Map.of("fileName", fileName, "message", "Subida exitosa"));
        } catch (IOException ex) {
            System.err.println("   ❌ Error upload: " + ex.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
    }

    // --- DOWNLOAD CON LOGS Y LIMPIEZA ---
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            System.out.println("📤 [DOWNLOAD] Solicitud: " + fileName);
            
            // Limpieza de ruta
            Path pathObj = Paths.get(fileName);
            String cleanName = pathObj.getFileName().toString();
            System.out.println("   🧹 Nombre limpiado: " + cleanName);

            Path filePath = this.fileStorageLocation.resolve(cleanName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                System.out.println("   ✅ Enviando archivo...");
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                System.out.println("   ❌ Archivo NO encontrado en disco.");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- ENDPOINTS DELEGADOS ---
    @GetMapping("/aprobacion")
    public List<AprobacionDTO> getListaDeAprobacion() { return programaService.listarProgramasParaAprobacion(); }
    
    @GetMapping("/{id}")
    public Programa buscarProgramaPorId(@PathVariable Long id) { return programaService.buscarProgramaPorId(id); }
    
    @PutMapping("/{id}/estado")
    public String actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> p) { return programaService.actualizarEstadoPrograma(id, p.get("estado"), 1L); }
    
    @PostMapping
    public String crearPrograma(@RequestBody Programa p) { return programaService.crearPrograma(p, 1L); }
    
    @PutMapping("/{id}")
    public String modificarPrograma(@PathVariable Long id, @RequestBody Programa p) { p.setId(id); return programaService.modificarPrograma(p, 1L); }
    
    @DeleteMapping("/{id}")
    public String borrarPrograma(@PathVariable Long id) { return programaService.borrarPrograma(id, 1L); }
    
    @GetMapping("/dias")
    public List<Dia> listarDiasAsignados() { return programaService.listarTodosLosDias(); }
    
    @DeleteMapping("/dias/{idDia}")
    public String quitarDia(@PathVariable Long idDia) { return programaService.quitarDia(idDia, 1L); }
    
    @GetMapping("/control-emision")
    public ControlEmisionDTO getControlEmision() { return programaService.getControlEmisionDashboard(); }
    
    @GetMapping("/parrilla-semanal")
    public Map<String, List<ParrillaDTO>> getParrillaSemanal() { return programaService.obtenerParrillaSemanal(); }
}