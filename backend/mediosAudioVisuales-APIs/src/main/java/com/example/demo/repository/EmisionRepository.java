package com.example.demo.repository;

import com.example.demo.dto.Emision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class EmisionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

   
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

    
    public Emision buscarEmisionPorId(Long idEmision) {
       
        String sql = "SELECT * FROM emisiones WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new EmisionRowMapper(), idEmision);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    
    public List<Emision> listarTodasLasEmisiones() {
        String sql = "CALL s('emisiones', null, @mensaje)";
        return jdbcTemplate.query(sql, new EmisionRowMapper());
    }


    private static class EmisionRowMapper implements RowMapper<Emision> {
        @Override
        public Emision mapRow(ResultSet rs, int rowNum) throws SQLException {
            Emision emision = new Emision();
            emision.setId(rs.getLong("id"));
            emision.setEnVivo(rs.getBoolean("enVivo"));
            emision.setIdPrograma(rs.getLong("idPrograma"));
            return emision;
        }
    }
}