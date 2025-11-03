package com.example.demo.repository;

import com.example.demo.dto.Encuesta; //
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EncuestaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // --- MÉTODO DE ESCRITURA (CUD) USANDO SimpleJdbcCall ---
    // (Llama al SP 'cen')

    /**
     * Llama al procedimiento almacenado 'cen' para crear una nueva encuesta.
     * Procedimiento: cen(IN preguntar1, IN idU, OUT mensaje, IN idUs, OUT idE)
     *
     */
    public Long crearEncuesta(Encuesta encuesta, Long idUsuarioAuditoria) {
        
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("cen")
                .declareParameters(
                        new SqlParameter("preguntar1", Types.VARCHAR),
                        new SqlParameter("idU", Types.BIGINT),
                        new SqlParameter("idUs", Types.BIGINT),
                        new SqlOutParameter("mensaje", Types.VARCHAR),
                        new SqlOutParameter("idE", Types.BIGINT) // El ID que queremos recuperar
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("preguntar1", encuesta.getPreguntar());
        inParams.put("idU", encuesta.getIdUsuario());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        
        // Devolvemos el ID de la encuesta creada
        return (Long) outParams.get("idE");
    }

    // --- MÉTODOS DE LECTURA (R) USANDO JdbcTemplate + RowMapper ---
    // (Igual que tu UsuarioRepository usa 's')

    /**
     * Lista todas las encuestas llamando al procedimiento 's'.
     * (Igual que tu método 'listarTodosLosUsuarios')
     */
    public List<Encuesta> listarTodasLasEncuestas() {
        // Llama al procedimiento 's' pasando 'encuesta' como nombre de la tabla
        String sql = "CALL s('encuesta', null, null, @mensaje)";
        
        // Usa el RowMapper para convertir cada fila en un objeto Encuesta
        return jdbcTemplate.query(sql, new EncuestaRowMapper());
    }

}

/**
 * Mapea una fila del ResultSet de la tabla 'encuesta' a un DTO 'Encuesta'.
 * (Igual que tu clase 'UsuarioRowMapper')
 */
class EncuestaRowMapper implements RowMapper<Encuesta> {
    @Override
    public Encuesta mapRow(ResultSet rs, int rowNum) throws SQLException {
        Encuesta encuesta = new Encuesta();
        
        // Mapeamos columna por columna
        encuesta.setId(rs.getLong("id"));
        encuesta.setPreguntar(rs.getString("preguntar"));
        encuesta.setIdUsuario(rs.getLong("idUsuario"));
        
        return encuesta;
    }
}