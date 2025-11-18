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
//En SegmentoRepository.java, junto a los otros imports
import com.example.demo.dto.SegmentoABMDTO;
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
        
        // 💥 ¡AGREGÁ ESTA LÍNEA QUE FALTABA! 💥
        inParams.put("orden1", segmento.getOrden());
        
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
        
        // --- 💥 ¡LÍNEA QUE FALTABA! 💥 ---
        inParams.put("orden1", segmento.getOrden());
        // --- 💥 ---
        
        inParams.put("titulo1", segmento.getTitulo());
        inParams.put("idP1", segmento.getIdPrograma());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

  
    public List<Segmento> listarTodosLosSegmentos() {
    	String sql = "SELECT * FROM segmentos";        
        return jdbcTemplate.query(sql, new SegmentoRowMapper());
    }
    public List<Segmento> listarSegmentosPorPrograma(Long idPrograma) {
        // Usamos SQL directo porque el SP 's' no soporta este filtro
        String sql = "SELECT * FROM segmentos WHERE idPrograma = ?";
        
        // Usamos el 'traductor' (SegmentoRowMapper) que ya tenías
        return jdbcTemplate.query(sql, new SegmentoRowMapper(), idPrograma);
    }
  
    public Segmento buscarSegmentoPorId(Long idSegmentoBuscado) {
        
        String sql = "SELECT * FROM segmentos WHERE id = ?";
        
        try {
            
            return jdbcTemplate.queryForObject(sql, new SegmentoRowMapper(), idSegmentoBuscado);
        } catch (Exception e) {
           
            return null; 
        }
    }
    public List<SegmentoABMDTO> listarSegmentosParaABM() { // <<-- ¡El tipo de retorno AHORA es SegmentoABMDTO!
        
        // Consulta SQL con LEFT JOIN (Tu consulta es correcta, la mantenemos)
        String sql = 
            "SELECT " +
            "   s.id, s.titulo, s.duracion, s.estadoAprobacion, " +
            "   p.nombre AS nombrePrograma " + // Usamos un alias
            "FROM " +
            "   segmentos s " +
            "LEFT JOIN " + // <<-- ESTO ES LO QUE PERMITE ver segmentos SIN programa
            "   programas p ON s.idPrograma = p.id " + 
            "ORDER BY s.id DESC"; 
            
        // 💥 CORRECCIÓN: Usar el SegmentoABMDTORowMapper
        return jdbcTemplate.query(sql, new SegmentoABMDTORowMapper()); 
    }
 // ✅ AGREGA ESTA CLASE (Junto a tus otros mappers)

 // EN SegmentoRepository.java (Añade esta clase)
 // Este mapper se encarga de leer el resultado del JOIN para el ABMDTO
 class SegmentoABMDTORowMapper implements RowMapper<SegmentoABMDTO> {
     @Override
     public SegmentoABMDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
         SegmentoABMDTO dto = new SegmentoABMDTO();
         
         dto.setId(rs.getLong("id"));
         dto.setTitulo(rs.getString("titulo"));
         dto.setDuracion(rs.getFloat("duracion"));
         dto.setEstadoAprobacion(rs.getString("estadoAprobacion"));
         
         // ¡CRÍTICO!: Lee la columna "nombrePrograma" del LEFT JOIN
         dto.setNombrePrograma(rs.getString("nombrePrograma")); 
         
         return dto;
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
            segmento.setOrden(rs.getInt("orden"));
            return segmento;
        }
    }
}


