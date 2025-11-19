package com.example.demo.repository;

import com.example.demo.dto.Encuesta;
import com.example.demo.dto.EncuestaResultado; 
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

    public Long crearEncuesta(Encuesta encuesta, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("cen")
                .declareParameters(
                        new SqlParameter("preguntar1", Types.VARCHAR),
                        new SqlParameter("idU", Types.BIGINT),
                        new SqlParameter("idUs", Types.BIGINT),
                        new SqlOutParameter("mensaje", Types.VARCHAR),
                        new SqlOutParameter("idE", Types.BIGINT) 
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("preguntar1", encuesta.getPreguntar());
        inParams.put("idU", encuesta.getIdUsuario());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);
        return (Long) outParams.get("idE");
    }

    public List<EncuestaResultado> buscarEncuestaConOpcionesYVotos(Long idEncuesta) {
        String sql = "CALL s('encuesta', ?, @mensaje)";
        return jdbcTemplate.query(sql, new EncuestaResultadoRowMapper(), idEncuesta);
    }

    // --- NUEVO MÉTODO PARA EL FEED ---
    public List<EncuestaResultado> listarTodasLasEncuestas() {
        // Enviamos NULL al SP para que entienda que queremos todas
        String sql = "CALL s('encuesta', NULL, @mensaje)";
        return jdbcTemplate.query(sql, new EncuestaResultadoRowMapper());
    }

    class EncuestaResultadoRowMapper implements RowMapper<EncuestaResultado> {
        @Override
        public EncuestaResultado mapRow(ResultSet rs, int rowNum) throws SQLException {
            EncuestaResultado dto = new EncuestaResultado();
            
            dto.setIdEncuesta(rs.getLong("idEncuesta"));
            dto.setPreguntar(rs.getString("preguntar"));
            dto.setIdCreador(rs.getLong("idCreador"));
            
            // --- LECTURA DE FECHA (Requiere actualización del SP 's') ---
            try {
                dto.setFechaCreacion(rs.getTimestamp("fechaCreacion"));
            } catch (Exception e) {
                // Ignorar si no viene la columna
            }

            dto.setIdOpcion(rs.getLong("idOpcion"));
            dto.setOpcion(rs.getString("opcion"));
            dto.setTotalVotos(rs.getLong("totalVotos"));
            
            return dto;
        }
    }
}