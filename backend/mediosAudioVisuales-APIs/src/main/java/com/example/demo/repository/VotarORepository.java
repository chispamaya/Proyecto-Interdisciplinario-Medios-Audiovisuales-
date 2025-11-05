package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Repository
public class VotarORepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP 'vo' para registrar un voto.
     * SP: vo(IN idO, IN idU, IN idE, IN idUs, OUT mensaje)
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