package com.example.demo.service;

import com.example.demo.dto.Dia;
import com.example.demo.repository.DiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import java.time.LocalDate; 
import java.util.List;

/**
 * Capa de Servicio para la lógica de negocio de Días de Programa.
 * Se encarga de la lógica de 'ABMProgramasForm.jsx' (asignación de días).
 */
@Service
public class DiaService {

    @Autowired
    private DiaRepository diaRepository;

    // --- Métodos para ABMProgramasForm.jsx ---

    /**
     * Lógica para OBTENER todos los días asignados a un programa específico.
     * (Llamado desde 'ABMProgramasForm.jsx' al cargar un programa).
     */
    public List<Dia> listarDiasPorPrograma(Long idPrograma) {
        // Esta línea ahora funciona
        return diaRepository.listarDiasPorPrograma(idPrograma);
    }

    /**
     * (LÓGICA DE NEGOCIO - MODO LENTO)
     * Actualiza la lista COMPLETA de días para un programa.
     * Esto es lo que 'ABMProgramasForm.jsx' usará al guardar.
     */
    @Transactional
    public void actualizarDiasParaPrograma(Long idPrograma, List<LocalDate> fechasNuevas, Long idUsuarioAuditoria) {
        
        // 1. Buscamos los días viejos que están en la BD
        // Esta línea ahora funciona
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
    }
}