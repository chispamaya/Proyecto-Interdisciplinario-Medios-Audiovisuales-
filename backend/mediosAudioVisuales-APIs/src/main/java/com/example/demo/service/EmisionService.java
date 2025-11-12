package com.example.demo.service;

import com.example.demo.dto.Emision;
import com.example.demo.repository.EmisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Capa de Servicio para la lógica de negocio de Emisiones.
 * Se encarga de la lógica de 'ControlDeEmision.jsx'.
 */
@Service
public class EmisionService {

    @Autowired
    private EmisionRepository emisionRepository;

    /**
     * Lógica para OBTENER el estado de todas las emisiones.
     * (Llamado desde 'ControlDeEmision.jsx' al cargar).
     */
    public List<Emision> listarEmisiones() {
        return emisionRepository.listarTodasLasEmisiones();
    }

    /**
     * Lógica para OBTENER una sola emisión por ID.
     */
    public Emision buscarEmision(Long id) {
        return emisionRepository.buscarEmisionPorId(id);
    }

    /**
     * (LÓGICA DE NEGOCIO CLAVE)
     * Pone un programa "En Vivo" (true).
     *
     * @param idEmision El ID de la emisión a modificar.
     * @param idUsuarioAuditoria El ID del usuario que realiza la acción.
     */
    @Transactional
    public String ponerEnVivo(Long idEmision, Long idUsuarioAuditoria) {
        
        // --- Lógica de Negocio ---
        // 1. Buscamos la emisión en la BD
        Emision emision = emisionRepository.buscarEmisionPorId(idEmision);
        if (emision == null) {
            throw new RuntimeException("La emisión no existe.");
        }

        // (Lógica futura) Podríamos verificar que no haya OTRA emisión "en vivo".
        
        // 3. Modificamos el objeto
        emision.setEnVivo(true);

        // 4. Guardamos los cambios (SP 'me')
        return emisionRepository.modificarEmision(emision, idUsuarioAuditoria);
    }
    
    /**
     * Saca un programa de "En Vivo" (false).
     *
     * @param idEmision El ID de la emisión a modificar.
     * @param idUsuarioAuditoria El ID del usuario que realiza la acción.
     */
    public String sacarDeVivo(Long idEmision, Long idUsuarioAuditoria) {
        
        // 1. Buscamos la emisión
        Emision emision = emisionRepository.buscarEmisionPorId(idEmision);
        if (emision == null) {
            throw new RuntimeException("La emisión no existe.");
        }
        
        // 2. Modificamos el objeto
        emision.setEnVivo(false);

        // 3. Guardamos los cambios (SP 'me')
        return emisionRepository.modificarEmision(emision, idUsuarioAuditoria);
    }
}