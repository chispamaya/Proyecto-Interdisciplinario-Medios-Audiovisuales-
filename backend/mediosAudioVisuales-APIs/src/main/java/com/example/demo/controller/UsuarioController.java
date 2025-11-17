package com.example.demo.controller;

// Importamos los DTOs que vamos a DEVOLVER
import com.example.demo.dto.EmpleadoDto;

import com.example.demo.dto.PerfilDTO;
import com.example.demo.dto.Usuario;
import com.example.demo.dto.GestionProgramaDTO;

// Importamos el "Cerebro" (Service)
import com.example.demo.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*; // ¡Importa TODAS las anotaciones de Web!

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
/**
 * @RestController: Le dice a Spring que esta clase es un Controller
 * y que todos sus métodos devolverán JSON automáticamente.
 * * @RequestMapping("/api/usuarios"): Define la URL "base" para todos
 * los métodos en esta clase (ej: http://localhost:8080/api/usuarios).
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // --- 1. Conectamos el "Cerebro" (Service) ---
    // El Controller SÓLO habla con el Service.
    @Autowired
    private UsuarioService usuarioService;

    
    // --- 2. Endpoints (Las URLs de tu API) ---

    /**
     * Endpoint para MOSTRAR la tabla del ABM de Empleados
     * - Método HTTP: GET
     * - URL: http://localhost:8080/api/usuarios
     */
    @GetMapping
    public List<EmpleadoDto> listarEmpleados() {
        // 1. Llama al "cerebro" (Service) para que haga el trabajo pesado
        // 2. Spring automáticamente convierte la List<> devuelta en un JSON
        return usuarioService.listarEmpleadosConPermisos();
    }

    /**
     * Endpoint para la pantalla de "Perfil"
     * - Método HTTP: GET
     * - URL: http://localhost:8080/api/usuarios/perfil/5 (donde 5 es el ID)
     *
     * @PathVariable: Le dice a Spring que tome el 'id' de la URL 
     * y lo "inyecte" en la variable 'idUsuario'.
     */
    @GetMapping("/perfil/{id}")
    public PerfilDTO obtenerPerfilDeUsuario(@PathVariable Long id) {
        return usuarioService.obtenerDatosPerfil(id);
    }

    /**
     * Endpoint para CREAR un usuario (Botón "Agregar" del ABM)
     * - Método HTTP: POST
     * - URL: http://localhost:8080/api/usuarios
     *
     * @RequestBody: Le dice a Spring que tome el JSON que envió React
     * y lo convierta en un objeto DTO 'Usuario'.
     */
    @PostMapping
    public String crearUsuario(@RequestBody Usuario nuevoUsuario) {
        // NOTA: ¡El ID de auditoría (quién crea) vendrá de la SEGURIDAD!
        // Como aún no tenemos seguridad, "hardcodeamos" (ponemos a mano)
        // que la acción la hizo el Administrador (ID 1L).
        Long idUsuarioQueCrea = 1L; 
        
        return usuarioService.crearUsuario(nuevoUsuario, idUsuarioQueCrea);
    }

    /**
     * Endpoint para EDITAR el rol (Pantalla "Editar Empleado")
     * - Método HTTP: PUT (PUT se usa para Actualizar)
     * - URL: http://localhost:8080/api/usuarios/rol
     *
     * @RequestBody Map<String, Long>: Recibimos un JSON simple, ej:
     * { "idUsuario": 5, "idNuevoRol": 8 }
     */
    @GetMapping("/gestion/{idUsuario}")
    public List<GestionProgramaDTO> getGestionMultimedia(@PathVariable Long idUsuario) {
        // Llama al método que SÍ está en UsuarioService
        return usuarioService.listarGestionProgramasPorUsuario(idUsuario);
    }
    @PutMapping("/rol")
    public String modificarRolUsuario(@RequestBody Map<String, Long> payload) {
        Long idUsuarioAModificar = payload.get("idUsuario");
        Long idNuevoRol = payload.get("idNuevoRol");
        Long idUsuarioQueModifica = (long) 1; // Asumimos Admin (esto vendrá de la seguridad)
        
        return usuarioService.modificarRolUsuario(idUsuarioAModificar, idNuevoRol, idUsuarioQueModifica);
    }
    
    /**
     * Endpoint para BORRAR un usuario (Botón "Borrar" del ABM)
     * - Método HTTP: DELETE
     * - URL: http://localhost:8080/api/usuarios/5 (donde 5 es el ID a borrar)
     */
    @DeleteMapping("/{id}")
    public String borrarUsuario(@PathVariable Long id) {
        Long idUsuarioQueBorra = 1L; // Asumimos Admin (esto vendrá de la seguridad)
        return usuarioService.borrarUsuario(id, idUsuarioQueBorra);
    }
}