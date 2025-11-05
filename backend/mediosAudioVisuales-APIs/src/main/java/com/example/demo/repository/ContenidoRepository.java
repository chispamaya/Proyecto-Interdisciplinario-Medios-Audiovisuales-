package com.example.demo.repository;

import com.example.demo.dto.Contenido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ContenidoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP cc (Crear Contenido).
     * SP: cc(IN formato1 varchar(50), IN rutaArchivo1 varchar(500), IN idU1 int, IN idUs int, ...)
     */
    public String crearContenido(Contenido contenido, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cc");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("formato1", contenido.getFormato());
        inParams.put("rutaArchivo1", contenido.getRutaArchivo());
        inParams.put("idU1", contenido.getIdUsuario());
        inParams.put("idUs", idUsuarioAuditoria); // Parámetro de auditoría

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * Llama al SP bc (Borrar Contenido).
     * SP: bc(IN id1 int, IN idUs int, ...)
     */
    public String borrarContenido(Long idContenidoAEliminar, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bc");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", idContenidoAEliminar);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }
    
    // NOTA: No existe 'mc' (Modificar Contenido) en tu DB.sql.
}