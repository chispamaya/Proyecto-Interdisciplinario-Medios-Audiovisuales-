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
    
    // --- 2. LECTURA DE TAGS POR CONTENIDO ---
    public List<Long> obtenerIdsTagsPorContenido(Long idContenido) {
        // Buscamos en la tabla intermedia contenido_tag
        String sql = "SELECT idTag FROM contenido_tag WHERE idContenido = ?";
        // Devuelve una lista de Longs (IDs)
        return jdbcTemplate.queryForList(sql, Long.class, idContenido);
    }

    // --- 3. NUEVO: VERIFICAR REACCIÓN DEL USUARIO (Like/Dislike/Nada) ---
    public Boolean obtenerReaccionUsuario(Long idContenido, Long idUsuario) {
        String sql = "SELECT likeOdislike FROM audiencia_con WHERE idContenido = ? AND idUsuario = ?";
        try {
            // Si encuentra algo, devuelve true (like) o false (dislike)
            return jdbcTemplate.queryForObject(sql, Boolean.class, idContenido, idUsuario);
        } catch (EmptyResultDataAccessException e) {
            // Si no encuentra nada, devuelve null
            return null; 
        }
    }

    // --- 4. MÉTODOS DE ESCRITURA (SP) ---

    // Crear Contenido (SP 'cc')
    public String crearContenido(Contenido contenido, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cc");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("formato1", contenido.getFormato());
        inParams.put("rutaArchivo1", contenido.getRutaArchivo());
        inParams.put("texto1", contenido.getTexto());
        inParams.put("idU1", contenido.getIdUsuario());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    // Borrar Contenido (SP 'bc')
    public String borrarContenido(Long id, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bc");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", id);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    // --- 5. FUNCIONALIDAD DE LIKE/DISLIKE (SP 'ld') ---
    public String valorarContenido(boolean esLike, Long idContenido, Long idUsuario, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("ld");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("likeOdislike1", esLike); // true = like, false = dislike
        inParams.put("idC", idContenido);
        inParams.put("idU", idUsuario);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    // --- 6. FUNCIONALIDAD BORRAR VALORACIÓN (SP 'bv') ---
    public String borrarValoracion(Long idContenido, Long idUsuario, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bv");
        
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("idC", idContenido);
        inParams.put("idU", idUsuario);
        inParams.put("idUs", idUsuarioAuditoria);
        
        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }
}