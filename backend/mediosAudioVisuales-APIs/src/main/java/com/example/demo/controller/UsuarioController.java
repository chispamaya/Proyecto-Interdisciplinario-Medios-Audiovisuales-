package com.example.demo.controller;

import com.example.demo.dto.EmpleadoDto;
import com.example.demo.dto.PerfilDTO;
import com.example.demo.dto.Usuario;
import com.example.demo.dto.GestionProgramaDTO;
import com.example.demo.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    // --- GETs ---

    @GetMapping
    public List<EmpleadoDto> listarEmpleados() {
        return usuarioService.listarEmpleadosConPermisos();
    }

    @GetMapping("/perfil/{id}")
    public PerfilDTO obtenerPerfilDeUsuario(@PathVariable Long id) {
        return usuarioService.obtenerDatosPerfil(id);
    }
    
    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.buscarUsuarioPorId(id);
    }

    @GetMapping("/gestion/{idUsuario}")
    public List<GestionProgramaDTO> getGestionMultimedia(@PathVariable Long idUsuario) {
        return usuarioService.listarGestionProgramasPorUsuario(idUsuario);
    }

    // --- POSTs ---

    @PostMapping
    public String crearUsuario(@RequestBody Usuario nuevoUsuario) {
        Long idUsuarioQueCrea = 1L; 
        return usuarioService.crearUsuario(nuevoUsuario, idUsuarioQueCrea);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");
        return usuarioService.login(email, password);
    }
    
    // --- PUTs ---

    /**
     * Endpoint existente para cambiar Rol.
     * Backend maneja internamente que no se pierda la password.
     */
    @PutMapping("/rol")
    public String modificarRolUsuario(@RequestBody Map<String, Long> payload) {
        Long idUsuarioAModificar = payload.get("idUsuario");
        Long idNuevoRol = payload.get("idNuevoRol");
        Long idUsuarioQueModifica = 1L; // Hardcodeado por ahora
        
        return usuarioService.modificarRolUsuario(idUsuarioAModificar, idNuevoRol, idUsuarioQueModifica);
    }
    
    /**
     * NUEVO ENDPOINT: Para cambiar contraseña.
     * Espera JSON: { "idUsuario": 5, "nuevaPassword": "miNuevaClave" }
     */
    @PutMapping("/contrasenia")
    public ResponseEntity<?> cambiarContrasenia(@RequestBody Map<String, Object> payload) {
        try {
            Object idObj = payload.get("idUsuario");
            Object passObj = payload.get("nuevaPassword");

            if (idObj == null || passObj == null) {
                return ResponseEntity.badRequest().body("Faltan datos (idUsuario o nuevaPassword).");
            }

            Long idUsuario = Long.valueOf(idObj.toString());
            String nuevaPassword = passObj.toString();

            String resultado = usuarioService.cambiarContrasenia(idUsuario, nuevaPassword);

            if (resultado.contains("éxito") || resultado.contains("actualizado")) {
                return ResponseEntity.ok(Map.of("mensaje", resultado));
            } else {
                // Si el mensaje no dice éxito, devolvemos error 500 con el msj
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultado);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    // --- DELETEs ---

    @DeleteMapping("/{id}")
    public String borrarUsuario(@PathVariable Long id) {
        Long idUsuarioQueBorra = 1L; 
        return usuarioService.borrarUsuario(id, idUsuarioQueBorra);
    }
}