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
     * SP: cc(IN formato1 varchar(50), IN rutaArchivo1 varchar(500), IN idU1 int, IN idUs int, ...)
     */
    public String crearContenido(Contenido contenido, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cc");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("formato1", contenido.getFormato());
        inParams.put("rutaArchivo1", contenido.getRutaArchivo());
        inParams.put("idU1", contenido.getIdUsuario());
        inParams.put("idUs", idUsuarioAuditoria); // Parámetro de auditoría

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * Llama al SP bc (Borrar Contenido).
     * SP: bc(IN id1 int, IN idUs int, ...)
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
     * (AÑADIDO PARA PRUEBAS) Llama al SP s('contenidos', null, @mensaje)
     */
    public List<Contenido> listarTodosLosContenidos() {
        String sql = "CALL s('contenidos', null, @mensaje)";
        return jdbcTemplate.query(sql, new ContenidoRowMapper());
    }
}

/**
 * (AÑADIDO PARA PRUEBAS) RowMapper para Contenido.
 */
class ContenidoRowMapper implements RowMapper<Contenido> {
    @Override
    public Contenido mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contenido contenido = new Contenido();
        contenido.setId(rs.getLong("id"));
        contenido.setFormato(rs.getString("formato"));
        contenido.setRutaArchivo(rs.getString("rutaArchivo"));
        contenido.setIdUsuario(rs.getLong("idUsuario"));
        return contenido;
    }
}