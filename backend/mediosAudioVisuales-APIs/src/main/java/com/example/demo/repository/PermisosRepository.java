package com.example.demo.repository;

import com.example.demo.dto.Permisos; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PermisosRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Lista TODOS los permisos de la BD.
     * Llama a SP 's' con id1=null.
     * SP: s(IN tabla, IN id1, OUT mensaje) [cite: chispamaya/proyecto-interdisciplinario-medios-audiovisuales-/Proyecto-Interdisciplinario-Medios-Audiovisuales--Codigo-fuente/DB.sql]
     */
    public List<Permisos> listarTodosLosPermisos() {
        // Tu SP 's' entra en el bloque ELSE y hace "SELECT * FROM permisos"
        String sql = "CALL s('permisos', null, @mensaje)";
        return jdbcTemplate.query(sql, new PermisosRowMapper());
    }

 

    /**
     * Busca los permisos (como String) de un Rol específico.
     * Llama a s' pasando el nombre del rol.
     */
    public List<String> buscarPermisosPorRol(String nombreRol) {
        // El SP 's' ahora acepta el nombre del rol en el parámetro 'tabla'
        String sql = "CALL s(?, null, @mensaje)";
        
        // Como el SP devuelve una sola columna de tipo String ("tipoPermiso"),
        // usamos queryForList con String.class
        return jdbcTemplate.queryForList(sql, String.class, nombreRol);
    }
}

/**
 * El "TRADUCTOR" que convierte una fila de 'permisos' (ResultSet)
 * en un objeto Java (Permisos).
 */
class PermisosRowMapper implements RowMapper<Permisos> {
    @Override
    public Permisos mapRow(ResultSet rs, int rowNum) throws SQLException {
        Permisos permiso = new Permisos();
        permiso.setId(rs.getLong("id"));
        permiso.setTipoPermiso(rs.getString("tipoPermiso"));
        return permiso;
    }
}