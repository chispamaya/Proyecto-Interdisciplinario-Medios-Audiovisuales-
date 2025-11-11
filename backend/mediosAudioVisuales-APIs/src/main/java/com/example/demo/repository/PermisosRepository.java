package com.example.demo.repository;

import com.example.demo.dto.Permisos;
import com.example.demo.dto.PermisosRol;
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

    public List<Permisos> listarTodosLosPermisos() {
        String sql = "CALL s('permisos', null, @mensaje)";
        return jdbcTemplate.query(sql, new PermisosRowMapper());
    }

    public List<String> buscarPermisosPorRol(String nombreRol) {
        String sql = "CALL s(?, null, @mensaje)";
        return jdbcTemplate.queryForList(sql, String.class, nombreRol);
    }

    public List<PermisosRol> listarTodasLasRelacionesPermisosRol() {
        String sql = "SELECT * FROM permisos_rol";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PermisosRol pr = new PermisosRol();
            pr.setIdRol(rs.getLong("idRol"));
            pr.setIdPermiso(rs.getLong("idPermiso"));
            return pr;
        });
    }

    // --- 💥 ARREGLO HECHO AQUÍ 💥 ---
    // La clase "Traductora" ahora vive ADENTRO del PermisosRepository
    // (La hicimos 'private static' porque es la mejor práctica)
    private static class PermisosRowMapper implements RowMapper<Permisos> {
        @Override
        public Permisos mapRow(ResultSet rs, int rowNum) throws SQLException {
            Permisos permiso = new Permisos();
            permiso.setId(rs.getLong("id"));
            permiso.setTipoPermiso(rs.getString("tipoPermiso"));
            return permiso;
        }
    }
    // --- AQUÍ TERMINA LA CLASE ANIDADA ---

} // <-- AQUÍ TERMINA LA CLASE PRINCIPAL (PermisosRepository)