package com.example.demo.repository;

import com.example.demo.dto.Dia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper; // (1) IMPORTAR
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet; // (2) IMPORTAR
import java.sql.SQLException; // (3) IMPORTAR
import java.time.LocalDate; // (4) IMPORTAR
import java.util.HashMap;
import java.util.List; // (5) IMPORTAR
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
     * Borra por el ID de la tabla 'dias'.
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
     * (NUEVO) Lista los días asignados a un programa específico.
     * Este es el método que faltaba y causaba el error.
     */
    public List<Dia> listarDiasPorPrograma(Long idPrograma) {
        // Como el SP 's' no puede filtrar por 'idPrograma', usamos SQL directo.
        String sql = "SELECT * FROM dias WHERE idPrograma = ?";
        return jdbcTemplate.query(sql, new DiaRowMapper(), idPrograma);
    }
    
    /**
     * (NUEVO) Lista todos los días de la parrilla.
     */
    public List<Dia> listarTodosLosDias() {
        String sql = "CALL s('dias', null, @mensaje)";
        return jdbcTemplate.query(sql, new DiaRowMapper());
    }
}

/**
 * (NUEVO) RowMapper para Dia.
 * Le dice a Spring cómo convertir una fila de la tabla 'dias'
 * en un objeto Dia.java.
 */
class DiaRowMapper implements RowMapper<Dia> {
    @Override
    public Dia mapRow(ResultSet rs, int rowNum) throws SQLException {
        Dia dia = new Dia();
        dia.setId(rs.getLong("id"));
        dia.setDia(rs.getDate("dia").toLocalDate()); // Convierte DATE de SQL a LocalDate de Java
        dia.setIdPrograma(rs.getLong("idPrograma"));
        return dia;
    }
}