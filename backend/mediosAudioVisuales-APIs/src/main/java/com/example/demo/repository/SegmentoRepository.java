package com.example.demo.repository;

import com.example.demo.dto.Segmento; // Importa tu DTO
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
public class SegmentoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String crearSegmento(Segmento segmento, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cs");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("estadoAprobacion1", segmento.getEstadoAprobacion());
        inParams.put("duracion1", segmento.getDuracion());
        inParams.put("titulo1", segmento.getTitulo());
        inParams.put("idP1", segmento.getIdPrograma());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        return (String) outParams.get("mensaje");
    }

    public String borrarSegmento(Long idSegmentoAEliminar, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bs");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", idSegmentoAEliminar);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

  
    public String modificarSegmento(Segmento segmento, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("ms");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", segmento.getId());
        inParams.put("estadoAprobacion1", segmento.getEstadoAprobacion());
        inParams.put("duracion1", segmento.getDuracion());
        inParams.put("titulo1", segmento.getTitulo());
        inParams.put("idP1", segmento.getIdPrograma());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

  
    public List<Segmento> listarTodosLosSegmentos() {
        String sql = "CALL s('segmentos', null, @mensaje)";
        
        return jdbcTemplate.query(sql, new SegmentoRowMapper());
    }

  
    public Segmento buscarSegmentoPorId(Long idSegmentoBuscado) {
        
        String sql = "SELECT * FROM segmentos WHERE id = ?";
        
        try {
            
            return jdbcTemplate.queryForObject(sql, new SegmentoRowMapper(), idSegmentoBuscado);
        } catch (Exception e) {
           
            return null; 
        }
    }
    class SegmentoRowMapper implements RowMapper<Segmento> {
        @Override
        public Segmento mapRow(ResultSet rs, int rowNum) throws SQLException {
            Segmento segmento = new Segmento();
            
            segmento.setId(rs.getLong("id"));
            segmento.setEstadoAprobacion(rs.getString("estadoAprobacion"));
            segmento.setDuracion(rs.getFloat("duracion"));
            segmento.setTitulo(rs.getString("titulo"));
            segmento.setIdPrograma(rs.getLong("idPrograma"));
            
            return segmento;
        }
    }
}

