package com.example.demo.service;

import com.example.demo.dto.Dia;
import com.example.demo.repository.DiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Capa de Servicio para la lógica de negocio de Días.
 * Maneja tanto la Parrilla (ArmadoParrillaHoraria.jsx) como la definición de programas (ABMProgramasForm.jsx).
 */
@Service
public class DiaService {

    @Autowired
    private DiaRepository diaRepository;

    // ---------------------------------------------------------
    // SECCIÓN 1: Métodos para ArmadoParrillaHoraria.jsx (Parrilla)
    // ---------------------------------------------------------

    /**
     * Crea una asignación de día individual.
     */
    public String crearAsignacionDia(Dia dia, Long idUsuarioAuditoria) {
        // (Aquí podría ir la lógica de validación de horarios si la activamos)
        return diaRepository.crearDia(dia, idUsuarioAuditoria);
    }

    /**
     * Borra una asignación de día individual por su ID.
     * ESTE ES EL MÉTODO QUE TE FALTABA.
     */
    public String borrarAsignacionDia(Long idDia, Long idUsuarioAuditoria) {
        return diaRepository.borrarDia(idDia, idUsuarioAuditoria);
    }

    /**
     * Lista todos los días para mostrar la parrilla completa.
     * El Controller lo llama como 'listarParrillaCompleta'.
     */
    public List<Dia> listarParrillaCompleta() {
        return diaRepository.listarTodosLosDias();
    }


    // ---------------------------------------------------------
    // SECCIÓN 2: Métodos para ABMProgramasForm.jsx (ABM)
    // ---------------------------------------------------------

    /**
     * Obtiene los días asignados a un programa específico.
     */
    public List<Dia> listarDiasPorPrograma(Long idPrograma) {
        return diaRepository.listarDiasPorPrograma(idPrograma);
    }

    /**
     * Actualiza masivamente los días de un programa (Borra todo lo viejo y crea lo nuevo).
     * Usa @Transactional para seguridad.
     */
    @Transactional
    public void actualizarDiasParaPrograma(Long idPrograma, List<LocalDate> fechasNuevas, Long idUsuarioAuditoria) {
        
        // 1. Buscamos los días viejos que están en la BD
        List<Dia> diasViejos = diaRepository.listarDiasPorPrograma(idPrograma);

        // 2. Borramos los días viejos UNO POR UNO
        for (Dia diaViejo : diasViejos) {
            diaRepository.borrarDia(diaViejo.getId(), idUsuarioAuditoria);
        }

        // 3. Creamos los días nuevos UNO POR UNO
        if (fechasNuevas != null) {
            for (LocalDate fecha : fechasNuevas) {
                Dia nuevoDia = new Dia();
                nuevoDia.setIdPrograma(idPrograma);
                nuevoDia.setDia(fecha);
                
                diaRepository.crearDia(nuevoDia, idUsuarioAuditoria);
            }
        }
    }
}