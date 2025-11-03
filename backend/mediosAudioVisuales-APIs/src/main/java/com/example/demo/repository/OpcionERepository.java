package com.example.demo.repository;

import com.example.demo.dto.OpcionE; //
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
public class OpcionERepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al procedimiento almacenado 'co' para crear una opción.
     * Procedimiento: co(IN idE, IN opcion1, IN cont, IN idUs, OUT mensaje)
     *
     */
    public String crearOpcion(OpcionE opcion, int contador, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("co")
                .declareParameters(
                        new SqlParameter("idE", Types.BIGINT),
                        new SqlParameter("opcion1", Types.VARCHAR),
                        new SqlParameter("cont", Types.INTEGER),
                        new SqlParameter("idUs", Types.BIGINT),
                        new SqlOutParameter("mensaje", Types.VARCHAR)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("idE", opcion.getIdEncuesta());
        inParams.put("opcion1", opcion.getOpcion());
        inParams.put("cont", contador);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * Llama al procedimiento almacenado 'vo' para registrar un voto.
     * Procedimiento: vo(IN idO, IN idU, IN idE, IN idUs, OUT mensaje)
     *
     */
    public String votarOpcion(Long idOpcion, Long idUsuarioVotante, Long idEncuesta, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("vo")
                .declareParameters(
                        new SqlParameter("idO", Types.BIGINT),
                        new SqlParameter("idU", Types.BIGINT),
                        new SqlParameter("idE", Types.BIGINT),
                        new SqlParameter("idUs", Types.BIGINT),
                        new SqlOutParameter("mensaje", Types.VARCHAR)
                );
        
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("idO", idOpcion);
        inParams.put("idU", idUsuarioVotante);
        inParams.put("idE", idEncuesta);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }
    
}

/**
 * Mapea una fila del ResultSet de la tabla 'opcion_e' a un DTO 'OpcionE'.
 */
class OpcionERowMapper implements RowMapper<OpcionE> {
    @Override
    public OpcionE mapRow(ResultSet rs, int rowNum) throws SQLException {
        OpcionE opcion = new OpcionE();
        opcion.setId(rs.getLong("id"));
        opcion.setIdEncuesta(rs.getLong("idEncuesta"));
        opcion.setOpcion(rs.getString("opcion"));
        return opcion;
    }
}