package com.example.demo.service;

import com.example.demo.dto.Dia;
import com.example.demo.repository.DiaRepository;
import com.example.demo.repository.ProgramaRepository; // Importamos ProgramaRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // (1) IMPORTAR

import java.time.LocalDate; // (2) IMPORTAR
import java.util.List;

/**
 * Capa de Servicio para la lógica de negocio de la Parrilla (Días).
 * Se encarga de la lógica de 'ArmadoParrillaHoraria.jsx'
 * y de 'ABMProgramasForm.jsx'.
 */
@Service
public class DiaService {

    @Autowired
    private DiaRepository diaRepository;

    @Autowired
    private ProgramaRepository programaRepository; 

    // --- Métodos para ArmadoParrillaHoraria.jsx ---

    /**
     * Lógica de negocio para asignar un programa a un día (SP 'cd').
     * (Llamado desde 'ArmadoParrillaHoraria.jsx')
     */
    public String crearAsignacionDia(Dia dia, Long idUsuarioAuditoria) {
        
        // (Lógica de validación de horarios iría aquí)
        // ...
        
        return diaRepository.crearDia(dia, idUsuarioAuditoria);
    }

    /**
     * Lógica de negocio para quitar un programa de un día (SP 'bd').
     * (Llamado desde 'ArmadoParrillaHoraria.jsx')
     */
    public String borrarAsignacionDia(Long idDia, Long idUsuarioAuditoria) {
        // Borra por el ID de la tabla 'dias'
        return diaRepository.borrarDia(idDia, idUsuarioAuditoria);
    }

    /**
     * Lógica de negocio para ver la parrilla completa.
     * (Llamado desde 'ArmadoParrillaHoraria.jsx')
     */
    public List<Dia> listarParrillaCompleta() {
        return diaRepository.listarTodosLosDias();
    }


    // --- Métodos para ABMProgramasForm.jsx ---

    /**
     * Lógica para OBTENER todos los días asignados a un programa específico.
     * (Llamado desde 'ABMProgramasForm.jsx' al cargar un programa).
     */
    public List<Dia> listarDiasPorPrograma(Long idPrograma) {
        return diaRepository.listarDiasPorPrograma(idPrograma);
    }

    /**
     * (LÓGICA DE NEGOCIO - MODO LENTO)
     * Actualiza la lista COMPLETA de días para un programa.
     * Esto es lo que 'ABMProgramasForm.jsx' usará al guardar.
     */
    @Transactional // (3) Operación de "Todo o Nada"
    public void actualizarDiasParaPrograma(Long idPrograma, List<LocalDate> fechasNuevas, Long idUsuarioAuditoria) {
        
        // 1. Buscamos los días viejos que están en la BD
        List<Dia> diasViejos = diaRepository.listarDiasPorPrograma(idPrograma);

        // 2. Borramos los días viejos UNO POR UNO (Modo Lento)
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
        // 4. Si algo falla (ej. uno de los 'crear' falla), @Transactional
        //    deshace (hace ROLLBACK) de todos los 'borrar' y 'crear' anteriores.
    }
}