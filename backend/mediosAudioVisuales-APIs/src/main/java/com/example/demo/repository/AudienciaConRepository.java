package com.example.demo.repository;

import com.example.demo.dto.AudienciaCon;
import com.example.demo.dto.ReporteAudienciaDTO; // (1) Importar el nuevo DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper; // (2) Importar RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet; // (3) Importar ResultSet
import java.sql.SQLException; // (4) Importar SQLException
import java.util.HashMap;
import java.util.List; // (5) Importar List
import java.util.Map;

@Repository
public class AudienciaConRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Llama al SP ld (Like o Dislike).
     */
    public String crearOModificarVoto(AudienciaCon voto, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("ld");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("likeOdislike1", voto.getLikeOdislike());
        inParams.put("idC", voto.getIdContenido());
        inParams.put("idU", voto.getIdUsuario());
        inParams.put("idUs", idUsuarioAuditoria); 

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * Llama al SP bv (Borrar Voto).
     */
    public String borrarVoto(Long idContenido, Long idUsuario, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("bv");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("idC", idContenido);
        inParams.put("idU", idUsuario);
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (String) outParams.get("mensaje");
    }

    /**
     * (NUEVO) Genera el reporte de Likes/Dislikes para 'ReportesAudencia.jsx'.
     *
     * Como el SP 's' genérico no puede hacer 'GROUP BY' ni 'SUM',
     * creamos la consulta SQL aquí.
     */
    public List<ReporteAudienciaDTO> getReporteAudiencia() {
        
        // (6) Este SQL cuenta los 'true' (Likes) y 'false' (Dislikes) 
        // y los agrupa por cada 'idContenido' (posteo).
        String sql = "SELECT " +
                     "    idContenido, " +
                     "    SUM(CASE WHEN likeOdislike = true THEN 1 ELSE 0 END) AS totalLikes, " +
                     "    SUM(CASE WHEN likeOdislike = false THEN 1 ELSE 0 END) AS totalDislikes " +
                     "FROM audiencia_con " +
                     "GROUP BY idContenido";

        // (7) Ejecutamos la consulta y le decimos a Spring que use nuestro
        // nuevo 'ReporteAudienciaRowMapper' para construir los objetos.
        return jdbcTemplate.query(sql, new ReporteAudienciaRowMapper());
    }
}

/**
 * (NUEVO) RowMapper para el Reporte.
 * Le enseña a Spring cómo convertir una fila del resultado SQL
 * en un objeto 'ReporteAudienciaDTO'.
 */
class ReporteAudienciaRowMapper implements RowMapper<ReporteAudienciaDTO> {
    @Override
    public ReporteAudienciaDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReporteAudienciaDTO reporte = new ReporteAudienciaDTO();
        
        // Mapeamos la columna 'idContenido' del SQL al campo 'idContenido' del DTO
        reporte.setIdContenido(rs.getLong("idContenido"));
        
        // Mapeamos la columna 'totalLikes' del SQL al campo 'totalLikes' del DTO
        reporte.setTotalLikes(rs.getLong("totalLikes"));
        
        // Mapeamos la columna 'totalDislikes' del SQL al campo 'totalDislikes' del DTO
        reporte.setTotalDislikes(rs.getLong("totalDislikes"));
        
        return reporte;
    }
}