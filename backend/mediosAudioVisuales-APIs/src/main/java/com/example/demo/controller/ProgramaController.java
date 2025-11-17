package com.example.demo.controller;

// Importamos los DTOs que vamos a recibir o devolver
import com.example.demo.dto.AprobacionDTO;
import com.example.demo.dto.ControlEmisionDTO;
import com.example.demo.dto.Dia;
import com.example.demo.dto.Programa;

// Importamos el "Cerebro" (Service)
import com.example.demo.service.ProgramaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin; // <-- ¡IMPORTA ESTO!


@CrossOrigin(origins = "http://localhost:5173")

/**
 * @RestController: Le dice a Spring que esto es un Controller y devolverá JSON.
 * @RequestMapping("/api/programas"): URL Base para todos los métodos de esta clase.
 */
@RestController
@RequestMapping("/api/programas")
public class ProgramaController {

    // --- 1. Conectamos el "Cerebro" (Service) ---
    @Autowired
    private ProgramaService programaService;

    
    // --- 2. Endpoints para el ABM de Programas ---

    /**
     * Endpoint para CREAR un Programa (SP 'cpr')
     * - Método: POST
     * - URL: /api/programas
     */
    @PostMapping
    public String crearPrograma(@RequestBody Programa nuevoPrograma) {
        // TODO: Reemplazar '1L' con el ID del usuario logueado (seguridad)
        Long idUsuarioQueCrea = 1L; 
        return programaService.crearPrograma(nuevoPrograma, idUsuarioQueCrea);
    }

    /**
     * Endpoint para EDITAR un Programa (SP 'mpr')
     * - Método: PUT
     * - URL: /api/programas/5
     */
    @PutMapping("/{id}")
    public String modificarPrograma(@PathVariable Long id, @RequestBody Programa programa) {
        programa.setId(id); // Aseguramos que el ID de la URL sea el usado
        Long idUsuarioQueModifica = 1L; // TODO: Reemplazar con ID de seguridad
        return programaService.modificarPrograma(programa, idUsuarioQueModifica);
    }

    /**
     * Endpoint para BORRAR un Programa (SP 'bpr')
     * - Método: DELETE
     * - URL: /api/programas/5
     */
    @DeleteMapping("/{id}")
    public String borrarPrograma(@PathVariable Long id) {
        Long idUsuarioQueBorra = 1L; // TODO: Reemplazar con ID de seguridad
        return programaService.borrarPrograma(id, idUsuarioQueBorra);
    }

    /**
     * Endpoint para BUSCAR un Programa por ID (para llenar el form de "Editar")
     * - Método: GET
     * - URL: /api/programas/5
     */
    @GetMapping("/{id}")
    public Programa buscarProgramaPorId(@PathVariable Long id) {
        return programaService.buscarProgramaPorId(id);
    }

    /**
     * Endpoint para LISTAR todos los Programas (para la tabla del ABM)
     * - Método: GET
     * - URL: /api/programas
     */
    @GetMapping
    public List<Programa> listarTodosLosProgramas() {
        return programaService.listarTodosLosProgramas();
    }

    
    // --- 3. Endpoints para "Armado Parrilla Horaria" ---

    /**
     * Endpoint para ASIGNAR un Día a un Programa (SP 'cd')
     * - Método: POST
     * - URL: /api/programas/dias
     */
    @PostMapping("/dias")
    public String asignarDia(@RequestBody Dia dia) {
        Long idUsuarioQueAsigna = 1L; // TODO: Reemplazar con ID de seguridad
        return programaService.asignarDia(dia, idUsuarioQueAsigna);
    }

    /**
     * Endpoint para QUITAR un Día de un Programa (SP 'bd')
     * - Método: DELETE
     * - URL: /api/programas/dias/10 (donde 10 es el ID de la fila 'dias')
     */
    @DeleteMapping("/dias/{idDia}")
    public String quitarDia(@PathVariable Long idDia) {
        Long idUsuarioQueQuita = 1L; // TODO: Reemplazar con ID de seguridad
        return programaService.quitarDia(idDia, idUsuarioQueQuita);
    }

    
    // --- 4. Endpoints para Pantallas de Lógica Combinada ---

    /**
     * Endpoint para "Estado de Aprobación"
     * - Método: GET
     * - URL: /api/programas/aprobacion
     */
    @GetMapping("/aprobacion")
    public List<AprobacionDTO> getListaDeAprobacion() {
        // Llama al método que combina dias, programas, auditoria y usuarios
        return programaService.listarProgramasParaAprobacion();
    }
    

    /**
     * Endpoint para "Control de Emisión"
     * - Método: GET
     * - URL: /api/programas/control-emision
     */
    @GetMapping("/control-emision")
    public ControlEmisionDTO getControlEmision() {
        // Llama al método que arma el DTO "Maestro"
        return programaService.getControlEmisionDashboard();
    }

    /**
     * Endpoint para "SACAR DEL AIRE" (Botón en)
     * - Método: PUT
     * - URL: /api/programas/control-emision/sacar-del-aire
     * (Recibe un JSON como: { "idEmision": 3 } )
     */
   
}