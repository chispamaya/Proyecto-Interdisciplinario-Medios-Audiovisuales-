package com.example.demo.repository;

import com.example.demo.dto.Tag;
import com.example.demo.dto.TagReporteDTO; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
public class TagRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // --- MÉTODOS DE ESCRITURA ---

    public String crearTag(Tag tag, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("ct");
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("tag1", tag.getTag());
        inParams.put("idUs", idUsuarioAuditoria);
        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    // --- MÉTODOS DE LECTURA ---

    public List<Tag> listarTodosLosTags() {
        String sql = "SELECT * FROM tags";        
        return jdbcTemplate.query(sql, new TagRowMapper());
    }

    public Tag buscarTagPorId(Long idTagBuscado) {
        String sql = "SELECT * FROM tags WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new TagRowMapper(), idTagBuscado);
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }
    
    public Long buscarIdTagPorNombre(String nombreTag) {
        String sql = "SELECT id FROM tags WHERE tag = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, nombreTag);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // --- REPORTE DE TAGS (Corrección para evitar nulos) ---
    public List<TagReporteDTO> obtenerReporteReaccionesPorTag() {
        String sql = """
            SELECT 
                t.tag,
                COALESCE(SUM(CASE WHEN ac.likeOdislike = 1 THEN 1 ELSE 0 END), 0) AS likes,
                COALESCE(SUM(CASE WHEN ac.likeOdislike = 0 THEN 1 ELSE 0 END), 0) AS dislikes
            FROM tags t
            LEFT JOIN contenido_tag ct ON t.id = ct.idTag
            LEFT JOIN audiencia_con ac ON ct.idContenido = ac.idContenido
            GROUP BY t.id, t.tag
            ORDER BY likes DESC
        """;
        // Quitamos el HAVING para que traiga todos los tags, aunque tengan 0 votos
        
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TagReporteDTO.class));
    }

    // --- ROW MAPPER ---
    public static class TagRowMapper implements RowMapper<Tag> {
        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            Tag tag = new Tag();
            tag.setId(rs.getLong("id"));
            tag.setTag(rs.getString("tag")); 
            return tag;
        }
    }
}