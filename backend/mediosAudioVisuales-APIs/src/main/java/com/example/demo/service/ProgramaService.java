package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProgramaService {

    @Autowired private ProgramaRepository programaRepository;
    @Autowired private AuditoriaRepository auditoriaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private DiaRepository diaRepository;
    @Autowired private EmisionRepository emisionRepository;
    @Autowired private SegmentoRepository segmentoRepository;

    // ==========================================
    // 🛠️ CREAR PROGRAMA + GUARDAR DÍAS (SOLUCIÓN FECHAS)
    // ==========================================
    public String crearPrograma(Programa nuevoPrograma, Long idUsuario) {
        // 1. Validaciones
        if (nuevoPrograma.getNombre() == null || nuevoPrograma.getNombre().isEmpty()) {
            return "Error: Nombre requerido.";
        }
        if (nuevoPrograma.getEstadoAprobacion() == null || nuevoPrograma.getEstadoAprobacion().isEmpty()) {
            nuevoPrograma.setEstadoAprobacion("En Revisión");
        }

        // 2. Guardar Programa (Llamada al SP)
        String resultado = programaRepository.crearPrograma(nuevoPrograma, idUsuario);

        // 3. 🚨 GUARDAR DÍAS (Lógica Nueva)
        // Como el SP no devuelve el ID, buscamos el último programa creado (por nombre/categoría o max ID)
        // Para este prototipo, usaremos una búsqueda por nombre reciente o asumiremos que es el último insertado.
        try {
            // Buscamos el programa recién creado para obtener su ID
            // (Idealmente el SP debería devolver el ID, pero usamos este workaround)
            List<Programa> todos = programaRepository.listarTodosLosProgramas();
            // Filtramos por nombre y tomamos el ID más alto (el más reciente)
            Programa programaCreado = todos.stream()
                .filter(p -> p.getNombre().equals(nuevoPrograma.getNombre()))
                .findFirst() // Como 'todos' viene ordenado DESC por ID, el primero es el correcto
                .orElse(null);

            if (programaCreado != null && nuevoPrograma.getDias() != null && !nuevoPrograma.getDias().isEmpty()) {
                for (Dia d : nuevoPrograma.getDias()) {
                    if (d.getDia() != null) {
                        d.setIdPrograma(programaCreado.getId());
                        diaRepository.crearDia(d, idUsuario);
                    }
                }
                System.out.println("✅ Días guardados para el programa ID: " + programaCreado.getId());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al guardar días: " + e.getMessage());
        }

        return resultado;
    }

    // ==========================================
    // 🔍 BUSCAR POR ID (Muestra Fechas)
    // ==========================================
    public Programa buscarProgramaPorId(Long id) {
        Programa p = programaRepository.buscarProgramaPorId(id);
        if (p != null) {
            List<Dia> todosLosDias = diaRepository.listarTodosLosDias();
            List<Dia> diasDelPrograma = todosLosDias.stream()
                .filter(d -> d.getIdPrograma() != null && d.getIdPrograma().equals(id))
                .collect(Collectors.toList());
            p.setDias(diasDelPrograma); 
        }
        return p;
    }

    // ==========================================
    // 📋 LISTA APROBACIÓN (Filtra Aprobados/Rechazados)
    // ==========================================
    public List<AprobacionDTO> listarProgramasParaAprobacion() {
        List<Programa> todos = programaRepository.listarTodosLosProgramas();
        List<Usuario> usuarios = usuarioRepository.listarTodosLosUsuarios();
        List<Auditoria> inserts = auditoriaRepository.buscarAuditoriaPorTablaYAccion("programas", "INSERT");

        Map<Long, String> mapaUsuarios = usuarios.stream().collect(Collectors.toMap(Usuario::getId, Usuario::getNombre));
        Map<Long, Long> mapaCreadores = inserts.stream().collect(Collectors.toMap(Auditoria::getRegistroAfectadoId, Auditoria::getUsuarioId, (a, b) -> a));

        List<AprobacionDTO> resultado = new ArrayList<>();

        for (Programa p : todos) {
            String estado = p.getEstadoAprobacion();
            // 🔽 FILTRO: Si ya no está pendiente, lo ocultamos
            if (estado != null && (estado.equalsIgnoreCase("Aprobado") || estado.equalsIgnoreCase("Rechazado"))) {
                continue; 
            }

            AprobacionDTO dto = new AprobacionDTO();
            dto.setIdPrograma(p.getId());
            dto.setTituloPrograma(p.getNombre());
            dto.setEstadoAprobacion(estado != null ? estado : "En Revisión");
            dto.setHoraInicio(p.getHoraInicio());
            dto.setHoraFin(p.getHoraFin());
            dto.setRutaArchivo(p.getRutaArchivo());
            dto.setRutaInforme(p.getRutaInforme());

            Long idCreador = mapaCreadores.get(p.getId());
            dto.setPropuestaDe(idCreador != null ? mapaUsuarios.getOrDefault(idCreador, "Desconocido") : "Desconocido");
            
            resultado.add(dto);
        }
        return resultado;
    }

    // --- MÉTODOS ABM BÁSICOS ---
    public String modificarPrograma(Programa p, Long u) { return programaRepository.modificarPrograma(p, u); }
    public String actualizarEstadoPrograma(Long id, String e, Long u) { return programaRepository.actualizarEstado(id, e, u); }
    public String borrarPrograma(Long id, Long u) { return programaRepository.borrarPrograma(id, u); }
    public List<Programa> listarTodosLosProgramas() { return programaRepository.listarTodosLosProgramas(); }
    public String asignarDia(Dia d, Long u) { return diaRepository.crearDia(d, u); }
    public String quitarDia(Long id, Long u) { return diaRepository.borrarDia(id, u); }
    public List<Dia> listarTodosLosDias() { return diaRepository.listarTodosLosDias(); }
    public List<GestionProgramaDTO> listarGestionProgramasPorUsuario(Long id) { return new ArrayList<>(); } // Simplificado
    public ControlEmisionDTO getControlEmisionDashboard() { return new ControlEmisionDTO(); } // Simplificado
    public Map<String, List<ParrillaDTO>> obtenerParrillaSemanal() { return new HashMap<>(); } // Simplificado
}