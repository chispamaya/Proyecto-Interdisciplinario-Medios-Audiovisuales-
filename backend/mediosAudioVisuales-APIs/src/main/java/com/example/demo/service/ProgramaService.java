package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.repository.ProgramaRepository;
import com.example.demo.repository.SegmentoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.AuditoriaRepository;
import com.example.demo.repository.DiaRepository;
import com.example.demo.repository.EmisionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
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

    // --- MÉTODOS ABM BÁSICOS ---

    public String crearPrograma(Programa nuevoPrograma, Long idUsuarioQueCrea) {
        if (nuevoPrograma.getNombre() == null || nuevoPrograma.getNombre().isEmpty()) {
            return "Error: El programa debe tener un nombre.";
        }
        if (nuevoPrograma.getEstadoAprobacion() == null || nuevoPrograma.getEstadoAprobacion().isEmpty()) {
            nuevoPrograma.setEstadoAprobacion("Pendiente"); 
        }
        return programaRepository.crearPrograma(nuevoPrograma, idUsuarioQueCrea);
    }

    public String modificarPrograma(Programa programa, Long idUsuarioQueModifica) {
        if (programa.getNombre() == null || programa.getNombre().isEmpty()) {
            return "Error: El programa debe tener un nombre.";
        }
        return programaRepository.modificarPrograma(programa, idUsuarioQueModifica);
    }

    public String actualizarEstadoPrograma(Long idPrograma, String nuevoEstado, Long idUsuario) {
        return programaRepository.actualizarEstado(idPrograma, nuevoEstado, idUsuario);
    }

    public String borrarPrograma(Long idProgramaABorrar, Long idUsuarioQueBorra) {
        return programaRepository.borrarPrograma(idProgramaABorrar, idUsuarioQueBorra);
    }

    public Programa buscarProgramaPorId(Long id) {
        return programaRepository.buscarProgramaPorId(id);
    }

    public List<Programa> listarTodosLosProgramas() {
        return programaRepository.listarTodosLosProgramas();
    }

    public String asignarDia(Dia dia, Long idUsuarioQueAsigna) {
        return diaRepository.crearDia(dia, idUsuarioQueAsigna);
    }

    public String quitarDia(Long idDia, Long idUsuarioQueQuita) {
        return diaRepository.borrarDia(idDia, idUsuarioQueQuita);
    }
    
    // 🔥 NUEVO: Recuperar lo guardado para ArmadoParrilla 🔥
    public List<Dia> listarTodosLosDias() {
        return diaRepository.listarTodosLosDias();
    }

    // --- LÓGICA: ESTADO Y APROBACIÓN ---
    public List<AprobacionDTO> listarProgramasParaAprobacion() {
        
        List<Dia> todosLosDias = diaRepository.listarTodosLosDias();
        List<Programa> todosLosProgramas = programaRepository.listarTodosLosProgramas();
        List<Usuario> todosLosUsuarios = usuarioRepository.listarTodosLosUsuarios();
        List<Auditoria> auditoriasDeProgramas = auditoriaRepository.buscarAuditoriaPorTablaYAccion("programas", "INSERT");

        Map<Long, Programa> mapaProgramas = todosLosProgramas.stream()
                .collect(Collectors.toMap(Programa::getId, programa -> programa));
        
        Map<Long, String> mapaUsuarios = todosLosUsuarios.stream()
                .collect(Collectors.toMap(Usuario::getId, Usuario::getNombre));

        Map<Long, Long> mapaPropuestas = auditoriasDeProgramas.stream()
                .collect(Collectors.toMap(
                    Auditoria::getRegistroAfectadoId, 
                    Auditoria::getUsuarioId,          
                    (idUsuarioExistente, idUsuarioNuevo) -> idUsuarioExistente 
                ));

        List<AprobacionDTO> resultadoFinal = new ArrayList<>();

        for (Dia dia : todosLosDias) {
            Programa programa = mapaProgramas.get(dia.getIdPrograma());
            if (programa == null) continue; 
            
            String estado = programa.getEstadoAprobacion();
            
            // Filtro: Ignorar APROBADO o RECHAZADO
            if (estado != null && (estado.equalsIgnoreCase("APROBADO") || estado.equalsIgnoreCase("RECHAZADO"))) {
                continue; 
            }
            
            Long idProponente = mapaPropuestas.get(programa.getId());
            String nombreProponente = mapaUsuarios.get(idProponente);
            
            AprobacionDTO dto = new AprobacionDTO();
            dto.setIdPrograma(programa.getId());
            dto.setIdDia(dia.getId());
            dto.setFechaEmision(dia.getDia()); 
            
            dto.setTituloPrograma(programa.getNombre());
            dto.setHoraInicio(programa.getHoraInicio()); 
            dto.setHoraFin(programa.getHoraFin());       
            dto.setEstadoAprobacion(programa.getEstadoAprobacion());
            
            dto.setPropuestaDe(nombreProponente != null ? nombreProponente : "Desconocido");
            
            dto.setRutaArchivo(programa.getRutaArchivo());
            dto.setRutaInforme(programa.getRutaInforme());

            resultadoFinal.add(dto);
        }

        return resultadoFinal;
    }

    // --- LÓGICA: GESTIÓN POR USUARIO ---
    public List<GestionProgramaDTO> listarGestionProgramasPorUsuario(Long idUsuario) {
        
        List<Programa> todosLosProgramas = programaRepository.listarTodosLosProgramas();
        List<Auditoria> auditorias = auditoriaRepository.buscarAuditoriaPorUsuarioYTTabla(
            idUsuario, 
            "programas", 
            "INSERT"
        );
        
        Map<Long, Programa> mapaProgramas = todosLosProgramas.stream()
                .collect(Collectors.toMap(Programa::getId, programa -> programa));
        
        List<GestionProgramaDTO> resultadoFinal = new ArrayList<>();
        
        for (Auditoria aud : auditorias) {
            Programa programa = mapaProgramas.get(aud.getRegistroAfectadoId());
            if (programa != null) {
                GestionProgramaDTO dto = new GestionProgramaDTO();
                dto.setIdPrograma(programa.getId());
                dto.setTitulo(programa.getNombre());
                dto.setEstadoAprobacion(programa.getEstadoAprobacion());
                dto.setFechaCreacion(aud.getFecha()); 
                
                if (programa.getHoraInicio() != null && programa.getHoraFin() != null) {
                    long duracionEnMinutos = Duration.between(programa.getHoraInicio(), programa.getHoraFin()).toMinutes();
                    dto.setDuracionEnMinutos(duracionEnMinutos);
                } else {
                    dto.setDuracionEnMinutos(0L); 
                }
                resultadoFinal.add(dto);
            }
        }
        return resultadoFinal; 
    }

    // --- LÓGICA: CONTROL DE EMISIÓN (DASHBOARD) ---
    public ControlEmisionDTO getControlEmisionDashboard() {
        
        ControlEmisionDTO dashboard = new ControlEmisionDTO();
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        // A. EN VIVO
        Emision emisionEnVivo = emisionRepository.listarTodasLasEmisiones().stream()
                .filter(Emision::getEnVivo) 
                .findFirst()
                .orElse(null);

        if (emisionEnVivo != null) {
            Programa programaEnVivo = programaRepository.buscarProgramaPorId(emisionEnVivo.getIdPrograma());
            
            if (programaEnVivo != null) {
                ControlEmisionDTO.ProgramaEnVivoInfo enVivoInfo = new ControlEmisionDTO.ProgramaEnVivoInfo();
                enVivoInfo.setIdEmision(emisionEnVivo.getId());
                enVivoInfo.setIdPrograma(programaEnVivo.getId());
                enVivoInfo.setTitulo(programaEnVivo.getNombre());
                enVivoInfo.setHoraInicio(programaEnVivo.getHoraInicio());
                enVivoInfo.setHoraFin(programaEnVivo.getHoraFin());
                dashboard.setEnVivo(enVivoInfo);

                List<Segmento> segmentos = segmentoRepository.listarSegmentosPorPrograma(programaEnVivo.getId());
                
                dashboard.setPublicidades(
                    segmentos.stream().map(seg -> {
                        ControlEmisionDTO.SegmentoInfo segInfo = new ControlEmisionDTO.SegmentoInfo();
                        segInfo.setTitulo(seg.getTitulo());
                        segInfo.setDuracion(seg.getDuracion());
                        return segInfo;
                    }).collect(Collectors.toList())
                );
            }
        }
        
        // B. PRÓXIMOS (AGENDA HOY)
        List<Dia> diasDeHoy = diaRepository.listarDiasPorFecha(hoy);
        List<Programa> todosLosProgramas = programaRepository.listarTodosLosProgramas();
        Map<Long, Programa> mapaProgramas = todosLosProgramas.stream()
                .collect(Collectors.toMap(Programa::getId, p -> p));

        List<ControlEmisionDTO.ProgramaInfo> proximos = new ArrayList<>();
        
        for (Dia dia : diasDeHoy) {
            Programa prog = mapaProgramas.get(dia.getIdPrograma());
            
            // 🔴 CORRECCIÓN: Verificar que HORA FIN sea posterior a AHORA
            if (prog != null && prog.getHoraFin() != null && prog.getHoraFin().isAfter(ahora)) {
                
                // Evitar duplicado si ya está en vivo
                if (emisionEnVivo != null && emisionEnVivo.getIdPrograma().equals(prog.getId())) {
                    continue;
                }

                ControlEmisionDTO.ProgramaInfo progInfo = new ControlEmisionDTO.ProgramaInfo();
                progInfo.setIdPrograma(prog.getId()); // Seteamos ID
                progInfo.setTitulo(prog.getNombre());
                progInfo.setHoraInicio(prog.getHoraInicio());
                progInfo.setHoraFin(prog.getHoraFin());
                proximos.add(progInfo);
            }
        }
        proximos.sort((p1, p2) -> p1.getHoraInicio().compareTo(p2.getHoraInicio()));
        dashboard.setProximos(proximos);
        
        return dashboard;
    }

    // --- 🔥 LÓGICA: PARRILLA SEMANAL (Para la nueva pantalla) 🔥 ---
    public Map<String, List<ParrillaDTO>> obtenerParrillaSemanal() {
        Map<String, List<ParrillaDTO>> parrilla = new HashMap<>();
        
        String[] diasSemana = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"};
        for (String dia : diasSemana) {
            parrilla.put(dia, new ArrayList<>());
        }

        // 1. Calcular rango de la semana actual
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate finSemana = inicioSemana.plusDays(6);

        // 2. Traer datos
        List<Dia> todosLosDias = diaRepository.listarTodosLosDias();
        List<Programa> todosLosProgramas = programaRepository.listarTodosLosProgramas();
        
        Map<Long, Programa> mapaProgramas = todosLosProgramas.stream()
                .collect(Collectors.toMap(Programa::getId, p -> p));

        // 3. Filtrar y Agrupar
        for (Dia diaDB : todosLosDias) {
            LocalDate fecha = diaDB.getDia(); 
            
            // Si la fecha está en esta semana...
            if (fecha != null && !fecha.isBefore(inicioSemana) && !fecha.isAfter(finSemana)) {
                
                Programa prog = mapaProgramas.get(diaDB.getIdPrograma());
                if (prog != null) {
                    ParrillaDTO item = new ParrillaDTO();
                    item.setNombrePrograma(prog.getNombre());
                    item.setHoraInicio(prog.getHoraInicio());
                    item.setHoraFin(prog.getHoraFin());

                    String nombreDia = traducirDia(fecha.getDayOfWeek());
                    
                    if (parrilla.containsKey(nombreDia)) {
                        parrilla.get(nombreDia).add(item);
                    }
                }
            }
        }
        
        // Ordenar por hora
        parrilla.forEach((k, v) -> v.sort((p1, p2) -> {
            if(p1.getHoraInicio() == null) return 1;
            if(p2.getHoraInicio() == null) return -1;
            return p1.getHoraInicio().compareTo(p2.getHoraInicio());
        }));

        return parrilla;
    }

    private String traducirDia(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return "LUNES";
            case TUESDAY: return "MARTES";
            case WEDNESDAY: return "MIERCOLES";
            case THURSDAY: return "JUEVES";
            case FRIDAY: return "VIERNES";
            case SATURDAY: return "SABADO";
            case SUNDAY: return "DOMINGO";
            default: return "";
        }
    }
}