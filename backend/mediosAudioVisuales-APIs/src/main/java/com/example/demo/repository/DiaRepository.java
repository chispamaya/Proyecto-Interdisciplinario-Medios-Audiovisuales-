package com.example.demo.repository;

import com.example.demo.dto.Dia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DiaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP cd (Crear Dia).
     * SP: cd(IN dia1 DATE, IN idP1 int, IN idUs int, ...)
     */
    public String crearDia(Dia dia, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cd");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("dia1", dia.getDia()); // Asumiendo que getDia() devuelve java.time.LocalDate
        inParams.put("idP1", dia.getIdPrograma());
        inParams.put("idUs", idUsuarioAuditoria); // Parámetro de auditoría

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * Llama al SP bd (Borrar Dia).
     * SP: bd(IN id1 int, IN idUs int, ...)
     */
    public String borrarDia(Long idDiaAEliminar, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bd");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", idDiaAEliminar);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }
    
    // NOTA: No existe 'md' (Modificar Dia) en tu DB.sql.
}