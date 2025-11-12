package com.example.demo.service;

import com.example.demo.dto.Plataforma;
import com.example.demo.repository.PlataformaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // <-- La etiqueta que lo define como "Cerebro"
public class PlataformaService {

    // --- 1. El "Brazo" (Conexión al Repository) ---
    @Autowired
    private PlataformaRepository plataformaRepository;

    
    // --- 2. Lógica de Negocio (Métodos del Service) ---

    /**
     * LÓGICA PARA "CREAR" (SP 'cpl')
     *
     */
    public String crearPlataforma(Plataforma nuevaPlataforma, Long idUsuarioQueCrea) {
        
        // --- AQUÍ VA LA LÓGICA DE NEGOCIO ---
        // (Ejemplo de regla: verificar que el nombre no esté vacío)
        if (nuevaPlataforma.getNombre() == null || nuevaPlataforma.getNombre().isEmpty()) {
            return "Error: La plataforma debe tener un nombre.";
        }
        
        // Si pasa las reglas, llama al "brazo"
        return plataformaRepository.crearPlataforma(nuevaPlataforma, idUsuarioQueCrea);
    }

    /**
     * LÓGICA PARA "EDITAR" (SP 'mpl')
     *
     */
    public String modificarPlataforma(Plataforma plataforma, Long idUsuarioQueModifica) {
        
        // (Aquí también podrías poner la misma validación)
        if (plataforma.getNombre() == null || plataforma.getNombre().isEmpty()) {
            return "Error: El nombre no puede estar vacío.";
        }
        
        // Si pasa las reglas, llama al "brazo"
        return plataformaRepository.modificarPlataforma(plataforma, idUsuarioQueModifica);
    }
    
    /**
     * LÓGICA PARA "BORRAR" (SP 'bpl')
     *
     */
    public String borrarPlataforma(Long idPlataformaABorrar, Long idUsuarioQueBorra) {
        // (Aquí podrías agregar lógica futura, ej: "No borrar si un programa la está usando")
        return plataformaRepository.borrarPlataforma(idPlataformaABorrar, idUsuarioQueBorra);
    }

    /**
     * LÓGICA PARA "LISTAR" (Para mostrar la tabla)
     */
    public List<Plataforma> listarTodasLasPlataformas() {
        // Simplemente llama al "brazo"
        return plataformaRepository.listarTodasLasPlataformas();
    }
    
    /**
     * LÓGICA PARA "BUSCAR UNA" (Para llenar el form de "Editar")
     */
    public Plataforma buscarPlataformaPorId(Long id) {
        return plataformaRepository.buscarPlataformaPorId(id);
    }

}