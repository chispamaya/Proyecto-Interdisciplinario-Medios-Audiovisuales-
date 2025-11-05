package com.example.demo.repository;

import com.example.demo.dto.Plataforma; // Asegurate de importar tu DTO
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
public class PlataformaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


   
    public String crearPlataforma(Plataforma plataforma, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cpl");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("nombre1", plataforma.getNombre());
        inParams.put("tipo1", plataforma.getTipo());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        return (String) outParams.get("mensaje");
    }

  
    public String borrarPlataforma(Long idPlataformaAEliminar, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bpl");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", idPlataformaAEliminar);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

   
    public String modificarPlataforma(Plataforma plataforma, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("mpl");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", plataforma.getId());
        inParams.put("nombre1", plataforma.getNombre());
        inParams.put("tipo1", plataforma.getTipo());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

   
    public List<Plataforma> listarTodasLasPlataformas() {
    	String sql = "CALL s('plataforma', null, @mensaje)";        
        return jdbcTemplate.query(sql, new PlataformaRowMapper());
    }

    
    public Plataforma buscarPlataformaPorId(Long idPlataformaBuscada) {
        
        String sql = "SELECT * FROM plataforma WHERE id = ?";
        
        try {
            return jdbcTemplate.queryForObject(sql, new PlataformaRowMapper(), idPlataformaBuscada);
        } catch (Exception e) {
            
            return null; 
        }
    }
}


class PlataformaRowMapper implements RowMapper<Plataforma> {
    @Override
    public Plataforma mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        Plataforma plataforma = new Plataforma();
        
        
        plataforma.setId(rs.getLong("id"));
        plataforma.setNombre(rs.getString("nombre"));
        plataforma.setTipo(rs.getString("tipo"));
        
        return plataforma;
    }
}