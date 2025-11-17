package com.example.demo.repository;

import com.example.demo.dto.Dia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DiaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP cd (Crear Dia).
     */
    public String crearDia(Dia dia, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cd");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("dia1", dia.getDia()); 
        inParams.put("idP1", dia.getIdPrograma());
        inParams.put("idUs", idUsuarioAuditoria); 

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * Llama al SP bd (Borrar Dia).
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
     * Lista los días por fecha específica (para validación de horarios).
     */
    public List<Dia> listarDiasPorFecha(LocalDate fecha) {
        String sql = "SELECT * FROM dias WHERE dia = ?";
        return jdbcTemplate.query(sql, new DiaRowMapper(), fecha);
    }

    /**
     * Lista los días asignados a un programa específico.
     * (Este método es el que le faltaba a tu compañero)
     */
    public List<Dia> listarDiasPorPrograma(Long idPrograma) {
        String sql = "SELECT * FROM dias WHERE idPrograma = ?";
        return jdbcTemplate.query(sql, new DiaRowMapper(), idPrograma);
    }
    
    /**
     * Lista todos los días (para ver la parrilla completa).
     */
    public List<Dia> listarTodosLosDias() {
        String sql = "CALL s('dias', null, @mensaje)";
        return jdbcTemplate.query(sql, new DiaRowMapper());
    }
}

class DiaRowMapper implements RowMapper<Dia> {
    @Override
    public Dia mapRow(ResultSet rs, int rowNum) throws SQLException {
        Dia dia = new Dia();
        dia.setId(rs.getLong("id"));
        // Convertimos java.sql.Date a java.time.LocalDate
        dia.setDia(rs.getDate("dia").toLocalDate()); 
        dia.setIdPrograma(rs.getLong("idPrograma"));
        return dia;
    }
}