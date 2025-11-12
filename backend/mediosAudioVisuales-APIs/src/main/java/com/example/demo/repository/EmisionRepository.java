package com.example.demo.repository;

import com.example.demo.dto.Emision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException; // (1) IMPORTAR
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper; // (2) IMPORTAR
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet; // (3) IMPORTAR
import java.sql.SQLException; // (4) IMPORTAR
import java.util.HashMap;
import java.util.List; // (5) IMPORTAR
import java.util.Map;

@Repository
public class EmisionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP me (Modificar Emision).
     * Usado para poner una emisión "En Vivo" o sacarla.
     */
    public String modificarEmision(Emision emision, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("me");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", emision.getId());
        inParams.put("enVivo1", emision.getEnVivo());
        inParams.put("idPr1", emision.getIdPrograma());
        inParams.put("idUs", idUsuarioAuditoria); 

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * (NUEVO) Llama al SP s('emisiones', ...) para listar todas.
     * Necesario para que 'ControlDeEmision.jsx' muestre el estado actual.
     */
    public List<Emision> listarTodasLasEmisiones() {
        String sql = "CALL s('emisiones', null, @mensaje)";
        return jdbcTemplate.query(sql, new EmisionRowMapper());
    }

    /**
     * (NUEVO) Llama al SP s('emisiones', id) para buscar una.
     * Necesario para buscar la emisión ANTES de modificarla.
     */
    public Emision buscarEmisionPorId(Long idEmision) {
        String sql = "CALL s('emisiones', ?, @mensaje)";
        try {
            return jdbcTemplate.queryForObject(sql, new EmisionRowMapper(), idEmision);
        } catch (EmptyResultDataAccessException e) {
            return null; // Devuelve null si no la encuentra
        }
    }
}

/**
 * (NUEVO) RowMapper para Emision.
 * Le dice a Spring cómo convertir una fila de la tabla 'emisiones'
 * en un objeto Emision.java.
 */
class EmisionRowMapper implements RowMapper<Emision> {
    @Override
    public Emision mapRow(ResultSet rs, int rowNum) throws SQLException {
        Emision emision = new Emision();
        emision.setId(rs.getLong("id"));
        emision.setEnVivo(rs.getBoolean("enVivo"));
        emision.setIdPrograma(rs.getLong("idPrograma"));
        return emision;
    }
}