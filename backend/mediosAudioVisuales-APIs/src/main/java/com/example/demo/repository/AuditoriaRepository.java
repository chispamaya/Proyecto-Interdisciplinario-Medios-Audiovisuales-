package com.example.demo.repository;

import com.example.demo.dto.Auditoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AuditoriaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Auditoria> obtenerTodasLasAuditorias() {

        String sql = "CALL s('auditoria', null, @mensaje)";
        

        return jdbcTemplate.query(sql, new AuditoriaRowMapper());
    }

    public List<Auditoria> buscarAuditoriaPorTablaYAccion(String tabla, String accion) {
    
        String sql = "SELECT * FROM auditoria WHERE tablaM = ? AND accion = ?";
        
    
        return jdbcTemplate.query(sql, new AuditoriaRowMapper(), tabla, accion);
    }
    public List<Auditoria> buscarAuditoriaPorUsuarioYTTabla(Long idUsuario, String tabla, String accion) {
        String sql = "SELECT * FROM auditoria WHERE usuario_id = ? AND tablaM = ? AND accion = ?";
        
        return jdbcTemplate.query(sql, new AuditoriaRowMapper(), idUsuario, tabla, accion);
    }
    class AuditoriaRowMapper implements RowMapper<Auditoria> {
        @Override
        public Auditoria mapRow(ResultSet rs, int rowNum) throws SQLException {
            Auditoria auditoria = new Auditoria();
            
            auditoria.setId(rs.getLong("id"));
            auditoria.setAccion(rs.getString("accion"));
            auditoria.setUsuarioId(rs.getLong("usuario_id")); // Mapeo de DB a DTO
            auditoria.setTablaM(rs.getString("tablaM"));
            auditoria.setRegistroAfectadoId(rs.getLong("registro_afectado_id"));
            auditoria.setFecha(rs.getTimestamp("fecha").toLocalDateTime()); // Conversión de Timestamp a LocalDateTime
            
            return auditoria;
        }
    }
}

