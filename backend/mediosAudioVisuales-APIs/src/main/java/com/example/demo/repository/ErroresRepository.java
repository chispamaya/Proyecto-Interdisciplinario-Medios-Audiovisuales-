package com.example.demo.repository;

import com.example.demo.dto.Errores; //
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
// ¡¡IMPORTANTE!! Hay que importar Timestamp
import java.sql.Timestamp; 

@Repository
public class ErroresRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Lista TODOS los errores registrados en la BD.
     * Llama a SP 's' con id1=null.
     * SP: s(IN tabla, IN id1, OUT mensaje)
     */
    public List<Errores> listarTodosLosErrores() {
        String sql = "CALL s('errores', null, @mensaje)";
        return jdbcTemplate.query(sql, new ErroresRowMapper());
    }
}

/**
 * El "TRADUCTOR" que convierte una fila de 'errores' (ResultSet)
 * en un objeto Java (Errores).
 */
class ErroresRowMapper implements RowMapper<Errores> {

    @Override
    public Errores mapRow(ResultSet rs, int rowNum) throws SQLException {
        Errores error = new Errores();
        
        error.setId(rs.getLong("id"));
        error.setTipo(rs.getString("tipo"));
        error.setMensaje(rs.getString("mensaje"));
        
        // 1. Sacamos el Timestamp (formato de BD)
        Timestamp ts = rs.getTimestamp("fechaYHora");
        
        // 2. Lo convertimos a LocalDateTime (formato de tu DTO)
    
        error.setFechaYHora(ts.toLocalDateTime());

        
        error.setIdUsuario(rs.getLong("idUsuario"));
        return error;
    }
}