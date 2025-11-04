package com.example.demo.repository;

import com.example.demo.dto.AudienciaCon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AudienciaConRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP ld (Like o Dislike).
     * SP: ld(IN likeOdislike1 boolean, IN idC int, IN idU int, IN idUs int, OUT mensaje varchar(50))
     */
    public String crearOModificarVoto(AudienciaCon voto, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("ld");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("likeOdislike1", voto.getLikeOdislike());
        inParams.put("idC", voto.getIdContenido());
        inParams.put("idU", voto.getIdUsuario());
        inParams.put("idUs", idUsuarioAuditoria); // Parámetro de auditoría

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * Llama al SP bv (Borrar Voto).
     * SP: bv(IN idC int, IN idU int, IN idUs int, OUT mensaje varchar(50))
     */
    public String borrarVoto(Long idContenido, Long idUsuario, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bv");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("idC", idContenido);
        inParams.put("idU", idUsuario);
        inParams.put("idUs", idUsuarioAuditoria); // Parámetro de auditoría

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }
}