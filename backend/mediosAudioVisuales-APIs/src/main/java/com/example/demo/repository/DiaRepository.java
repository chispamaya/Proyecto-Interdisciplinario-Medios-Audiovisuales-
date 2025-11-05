package com.example.demo.repository;

import com.example.demo.dto.Dia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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

    /**
     * (AÑADIDO PARA PRUEBAS) Llama al SP s('dias', null, @mensaje)
     */
    public List<Dia> listarTodosLosDias() {
        String sql = "CALL s('dias', null, @mensaje)";
        return jdbcTemplate.query(sql, new DiaRowMapper());
    }
}

/**
 * (AÑADIDO PARA PRUEBAS) RowMapper para Dia.
 */
class DiaRowMapper implements RowMapper<Dia> {
    @Override
    public Dia mapRow(ResultSet rs, int rowNum) throws SQLException {
        Dia dia = new Dia();
        dia.setId(rs.getLong("id"));
        dia.setDia(rs.getDate("dia").toLocalDate());
        dia.setIdPrograma(rs.getLong("idPrograma"));
        return dia;
    }
}