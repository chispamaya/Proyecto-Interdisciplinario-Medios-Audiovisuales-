package com.example.demo.repository;

import com.example.demo.dto.Contenido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ContenidoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // --- 1. LECTURA DE CONTENIDOS ---
    public List<Contenido> listarTodosLosContenidos() {
        String sql = "SELECT * FROM contenidos";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Contenido.class));
    }

    public List<Contenido> listarContenidosPorUsuario(Long idUsuario) {
        String sql = "SELECT * FROM contenidos WHERE idUsuario = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Contenido.class), idUsuario);
    }
    
    public List<Long> obtenerIdsTagsPorContenido(Long idContenido) {
        String sql = "SELECT idTag FROM contenido_tag WHERE idContenido = ?";
        return jdbcTemplate.queryForList(sql, Long.class, idContenido);
    }

    public Boolean obtenerReaccionUsuario(Long idContenido, Long idUsuario) {
        String sql = "SELECT likeOdislike FROM audiencia_con WHERE idContenido = ? AND idUsuario = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Boolean.class, idContenido, idUsuario);
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }

    // --- 4. MÉTODOS DE ESCRITURA (CORREGIDO PARA DETECTAR ERRORES) ---

    public String crearContenido(Contenido contenido, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cc");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("formato1", contenido.getFormato());
        inParams.put("rutaArchivo1", contenido.getRutaArchivo());
        inParams.put("texto1", contenido.getTexto());
        inParams.put("idU1", contenido.getIdUsuario());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        
        // --- VALIDACIÓN DE ERROR ---
        String mensaje = (String) outParams.get("mensaje");
        if (mensaje != null && (mensaje.startsWith("Error") || mensaje.contains("Ocurrio un error"))) {
            throw new RuntimeException("Error BD: " + mensaje);
        }
        
        return mensaje;
    }

    public String borrarContenido(Long id, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bc");
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", id);
        inParams.put("idUs", idUsuarioAuditoria);
        Map<String, Object> outParams = jdbcCall.execute(inParams);
        
        String mensaje = (String) outParams.get("mensaje");
        if (mensaje != null && mensaje.contains("Ocurrio un error")) {
             throw new RuntimeException(mensaje);
        }
        return mensaje;
    }

    public String valorarContenido(boolean esLike, Long idContenido, Long idUsuario, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("ld");
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("likeOdislike1", esLike);
        inParams.put("idC", idContenido);
        inParams.put("idU", idUsuario);
        inParams.put("idUs", idUsuarioAuditoria);
        Map<String, Object> outParams = jdbcCall.execute(inParams);
        
        String mensaje = (String) outParams.get("mensaje");
        if (mensaje != null && mensaje.contains("Ocurrio un error")) {
             throw new RuntimeException(mensaje);
        }
        return mensaje;
    }

    public String borrarValoracion(Long idContenido, Long idUsuario, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bv");
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("idC", idContenido);
        inParams.put("idU", idUsuario);
        inParams.put("idUs", idUsuarioAuditoria);
        Map<String, Object> outParams = jdbcCall.execute(inParams);
        
        String mensaje = (String) outParams.get("mensaje");
        if (mensaje != null && mensaje.contains("Ocurrio un error")) {
             throw new RuntimeException(mensaje);
        }
        return mensaje;
    }
}