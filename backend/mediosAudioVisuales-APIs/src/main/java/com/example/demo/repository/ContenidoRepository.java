package com.example.demo.repository;

import com.example.demo.dto.Contenido;
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
public class ContenidoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP cc (Crear Contenido).
     * Parámetros: cc(formato1, rutaArchivo1, texto1, idU1, idUs, @mensaje)
     */
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

    /**
     * Llama al SP bc (Borrar Contenido).
     */
    public String borrarContenido(Long idContenidoAEliminar, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bc");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("id1", idContenidoAEliminar);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * Lista TODO el contenido (usando SP 's').
     */
    public List<Contenido> listarTodosLosContenidos() {
        String sql = "CALL s('contenidos', null, @mensaje)";
        return jdbcTemplate.query(sql, new ContenidoRowMapper());
    }

    /**
     * Lista contenido FILTRADO por usuario.
     */
    public List<Contenido> listarContenidosPorUsuario(Long idUsuario) {
        String sql = "SELECT * FROM contenidos WHERE idUsuario = ?";
        return jdbcTemplate.query(sql, new ContenidoRowMapper(), idUsuario);
    }
}

/**
 * RowMapper adaptado a las columnas reales (formato, rutaArchivo, texto) de la tabla 'contenidos'.
 */
class ContenidoRowMapper implements RowMapper<Contenido> {
    @Override
    public Contenido mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contenido contenido = new Contenido();
        contenido.setId(rs.getLong("id"));
        contenido.setFormato(rs.getString("formato"));
        contenido.setRutaArchivo(rs.getString("rutaArchivo"));
        contenido.setTexto(rs.getString("texto")); 
        contenido.setIdUsuario(rs.getLong("idUsuario"));
        contenido.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
        return contenido;
    }
}