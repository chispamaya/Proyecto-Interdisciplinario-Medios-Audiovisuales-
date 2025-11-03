package com.example.demo.repository;

import com.example.demo.dto.Rol; // Importa tu DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RolRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Rol> listarTodosLosRoles() {
        String sql = "CALL s('rol', null, null, @mensaje)";
        
        return jdbcTemplate.query(sql, new RolRowMapper());
    }

   
    public Rol buscarRolPorId(Long idRolBuscado) {
        

        String sql = "SELECT * FROM rol WHERE id = ?";
        
        try {
            
            return jdbcTemplate.queryForObject(sql, new RolRowMapper(), idRolBuscado);
        } catch (Exception e) {
      
            return null; 
        }
    }
}


class RolRowMapper implements RowMapper<Rol> {
    @Override
    public Rol mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        Rol rol = new Rol();
        
        
        rol.setId(rs.getLong("id"));
        rol.setNombre(rs.getString("nombre"));
    
        return rol;
    }
}