package com.example.demo.repository;

import com.example.demo.dto.Encuesta;
import com.example.demo.dto.EncuestaResultado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EncuestaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 1. MÉTODO DE CREACIÓN (SP 'cen')
     * CORREGIDO: Declaramos explícitamente los parámetros para evitar errores
     * cuando los OUT están mezclados con los IN.
     */
    public Long crearEncuesta(Encuesta encuesta, Long idUsuarioAuditoria) {
        // Configuramos la llamada explícitamente
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("cen")
                .withoutProcedureColumnMetaDataAccess() // Desactivamos auto-detección para mandar nosotros el orden
                .declareParameters(
                    new SqlParameter("preguntar1", Types.VARCHAR),
                    new SqlParameter("idU", Types.INTEGER),
                    new SqlOutParameter("mensaje", Types.VARCHAR), // OUT mensaje está en el medio
                    new SqlParameter("idUs", Types.INTEGER),
                    new SqlOutParameter("idE", Types.INTEGER)      // OUT idE al final
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("preguntar1", encuesta.getPreguntar());
        inParams.put("idU", encuesta.getIdUsuario());
        inParams.put("idUs", idUsuarioAuditoria);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        // 1. VERIFICAMOS SI HUBO ERROR EN BD
        String mensajeBD = (String) outParams.get("mensaje");
        if (mensajeBD != null && (mensajeBD.startsWith("Error") || mensajeBD.contains("Ocurrio un error"))) {
            throw new RuntimeException("Error BD: " + mensajeBD);
        }

        // 2. VERIFICAMOS EL ID
        if (outParams.get("idE") != null) {
            return ((Number) outParams.get("idE")).longValue();
        } else {
            throw new RuntimeException("Error: La base de datos no devolvió el ID de la encuesta.");
        }
    }

    @SuppressWarnings("unchecked")
    public List<EncuestaResultado> buscarEncuestaConOpcionesYVotos(Long idEncuesta) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("s")
                .returningResultSet("resultados", BeanPropertyRowMapper.newInstance(EncuestaResultado.class));

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("tabla", "encuesta");
        inParams.put("id1", idEncuesta);

        Map<String, Object> out = jdbcCall.execute(inParams);
        return (List<EncuestaResultado>) out.get("resultados");
    }

    public List<EncuestaResultado> listarTodasLasEncuestas() {
        return buscarEncuestaConOpcionesYVotos(null);
    }

    public Long obtenerOpcionVotadaPorUsuario(Long idEncuesta, Long idUsuario) {
        String sql = "SELECT v.idOpcion FROM votar_o v " +
                     "JOIN opcion_e o ON v.idOpcion = o.id " +
                     "WHERE o.idEncuesta = ? AND v.idUsuario = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, idEncuesta, idUsuario);
        } catch (Exception e) {
            return null;
        }
    }
}