package com.example.demo.repository;

import com.example.demo.dto.contenidoTag; // Asegúrate que el nombre de tu DTO sea este
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ContenidoTagRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP cct (Crear Contenido-Tag).
     * SP: cct(IN idC1 int, IN idT1 int, IN idUs int, ...)
     */
    public String crearContenidoTag(contenidoTag contenidoTag, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cct");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("idC1", contenidoTag.getIdContenido());
        inParams.put("idT1", contenidoTag.getIdTag());
        inParams.put("idUs", idUsuarioAuditoria); // Parámetro de auditoría

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * Llama al SP bct (Borrar Contenido-Tag).
     * SP: bct(IN idC1 int, IN idUs int, ...)
     */
    public String borrarTagsPorContenido(Long idContenido, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bct");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("idC1", idContenido);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }
}