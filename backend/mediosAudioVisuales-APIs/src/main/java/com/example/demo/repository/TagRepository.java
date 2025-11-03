package com.example.demo.repository;

import com.example.demo.dto.Tag; // Importa tu DTO
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
public class TagRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // --- MÉTODO DE ESCRITURA (CUD) ---

    public String crearTag(Tag tag, Long idUsuarioAuditoria) {
       
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("ct");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("tag1", tag.getTag());
        inParams.put("idUs", idUsuarioAuditoria);

        
        Map<String, Object> outParams = jdbcCall.execute(inParams);

        return (String) outParams.get("mensaje");
    }

   


    public List<Tag> listarTodosLosTags() {
        String sql = "CALL s('tags', null, null, @mensaje)";
        
        return jdbcTemplate.query(sql, new TagRowMapper());
    }

 
    public Tag buscarTagPorId(Long idTagBuscado) {
        
        String sql = "SELECT * FROM tags WHERE id = ?";
        
        try {
            return jdbcTemplate.queryForObject(sql, new TagRowMapper(), idTagBuscado);
        } catch (Exception e) {
            return null; 
        }
    }
}

class TagRowMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        
        tag.setId(rs.getLong("id"));
        tag.setTag(rs.getString("tag")); 
        
        return tag;
    }
}