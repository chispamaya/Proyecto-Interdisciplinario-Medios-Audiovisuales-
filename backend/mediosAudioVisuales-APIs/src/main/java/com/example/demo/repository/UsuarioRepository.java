package com.example.demo.repository;

import com.example.demo.dto.Usuario;
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
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // --- MÉTODOS DE ESCRITURA (CUD) USANDO SimpleJdbcCall ---

    public String crearUsuario(Usuario usuario, Long idUsuarioAuditoria) {
    	
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cu");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("email1", usuario.getEmail());
        inParams.put("nombre1", usuario.getNombre());
        inParams.put("contrasenia1", usuario.getContrasenia());
        inParams.put("idRol1", usuario.getIdRol());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    public String borrarUsuario(Long idUsuarioAEliminar, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bu");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", idUsuarioAEliminar);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }


    public String modificarRolDeUsuario(Long idUsuarioAModificar, Long idNuevoRol, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("mu");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", idUsuarioAModificar);
        inParams.put("idRol1", idNuevoRol);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

  
    public Usuario buscarUsuarioPorId(Long idUsuarioBuscado) {
       
    	String sql = "CALL s('usuario', ?, @mensaje)";
    	
        try {
        	return jdbcTemplate.queryForObject(sql, new UsuarioRowMapper(), idUsuarioBuscado);        
        } catch (Exception e) {
            
            return null; 
        }
    }

   
    public List<Usuario> listarTodosLosUsuarios() {
    	String sql = "SELECT * FROM usuario";        
       
        return jdbcTemplate.query(sql, new UsuarioRowMapper());
    }
}

class UsuarioRowMapper implements RowMapper<Usuario> {
    @Override
    public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
        Usuario usuario = new Usuario();
        
        // Mapeamos columna por columna
        usuario.setId(rs.getLong("id"));
        usuario.setEmail(rs.getString("email"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setContrasenia(rs.getString("contrasenia"));
        usuario.setIdRol(rs.getLong("idRol"));
        
        return usuario;
    }
}
