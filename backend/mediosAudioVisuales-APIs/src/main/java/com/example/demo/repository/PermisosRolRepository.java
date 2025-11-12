package com.example.demo.repository;

import com.example.demo.dto.PermisosRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PermisosRolRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Lista TODAS las asignaciones de permisos a roles.
     * Llama a SP 's' con id1=null.
     * SP: s(IN tabla, IN id1, OUT mensaje) [cite: chispamaya/proyecto-interdisciplinario-medios-audiovisuales-/Proyecto-Interdisciplinario-Medios-Audiovisuales--Codigo-fuente/DB.sql]
     */
    public List<PermisosRol> listarTodasLasAsignaciones() {
        // Tu SP 's' entra en el bloque ELSE y hace "SELECT * FROM permisos_rol"
        String sql = "CALL s('permisos_rol', null, @mensaje)";
        return jdbcTemplate.query(sql, new PermisosRolRowMapper());
    }
    /**
     * El "TRADUCTOR" que convierte una fila de 'permisos_rol' (ResultSet)
     * en un objeto Java (PermisosRol).
     */
    class PermisosRolRowMapper implements RowMapper<PermisosRol> {
        @Override
        public PermisosRol mapRow(ResultSet rs, int rowNum) throws SQLException {
            PermisosRol pr = new PermisosRol();
            pr.setId(rs.getLong("id"));
            pr.setIdRol(rs.getLong("idRol"));
            pr.setIdPermiso(rs.getLong("idPermiso"));
            return pr;
        }
    }
 
}

