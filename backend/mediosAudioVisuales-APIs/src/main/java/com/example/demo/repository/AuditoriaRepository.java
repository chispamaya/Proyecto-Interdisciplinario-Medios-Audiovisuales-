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

    // NOTA: No hay métodos CUD (Crear, Update, Delete) porque la tabla
    // 'auditoria' se maneja 100% con Triggers en tu DB.sql.

    /**
     * Llama al SP s (Select) para obtener todos los registros de la tabla 'auditoria'.
     * Sigue el patrón de lectura del UsuarioRepository.
     */
    public List<Auditoria> obtenerTodasLasAuditorias() {
        // Llama a s('auditoria', null, @mensaje) - ajustado al SP 's' real
        String sql = "CALL s('auditoria', null, @mensaje)";
        
        // Usa el RowMapper personalizado
        return jdbcTemplate.query(sql, new AuditoriaRowMapper());
    }
}

/**
 * RowMapper para la entidad Auditoria.
 * Mapea las columnas de la DB (ej. 'usuario_id') a los campos del DTO (ej. 'usuarioId').
 */
class AuditoriaRowMapper implements RowMapper<Auditoria> {
    @Override
    public Auditoria mapRow(ResultSet rs, int rowNum) throws SQLException {
        Auditoria auditoria = new Auditoria();
        
        auditoria.setId(rs.getLong("id"));
        auditoria.setAccion(rs.getString("accion"));
        auditoria.setUsuarioId(rs.getLong("usuario_id")); // Mapeo de DB a DTO
        auditoria.setTablaM(rs.getString("tablaM"));
        auditoria.setRegistro_afectado_id(rs.getInt("registro_afectado_id"));
        auditoria.setFecha(rs.getTimestamp("fecha").toLocalDateTime()); // Conversión de Timestamp a LocalDateTime
        
        return auditoria;
    }
}