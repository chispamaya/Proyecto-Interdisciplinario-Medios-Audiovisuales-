package com.example.demo.repository;

import com.example.demo.dto.Emision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class EmisionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP me (Modificar Emision).
     * SP: me(IN id1 int, IN enVivo1 BOOLEAN, IN idPr1 int, IN idUs int, ...)
     */
    public String modificarEmision(Emision emision, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("me");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", emision.getId());
        inParams.put("enVivo1", emision.getEnVivo());
        inParams.put("idPr1", emision.getIdPrograma());
        inParams.put("idUs", idUsuarioAuditoria); // Parámetro de auditoría

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }
    
    // NOTA: No existen 'ce' (Crear Emision) ni 'be' (Borrar Emision) en tu DB.sql.
}