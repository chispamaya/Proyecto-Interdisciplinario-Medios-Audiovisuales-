package com.example.demo.repository;

import com.example.demo.dto.Encuesta;
import com.example.demo.dto.EncuestaResultado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EncuestaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 1. MÉTODO DE CREACIÓN (SP 'cen')
     * Recibe la encuesta y el usuario de auditoría.
     * Devuelve el ID de la nueva encuesta generado por la BD.
     */
    public Long crearEncuesta(Encuesta encuesta, Long idUsuarioAuditoria) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("cen");

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("preguntar1", encuesta.getPreguntar());
        inParams.put("idU", encuesta.getIdUsuario()); // ID del creador
        inParams.put("idUs", idUsuarioAuditoria);     // ID auditoría

        // Ejecutamos el SP
        Map<String, Object> outParams = jdbcCall.execute(inParams);

        // Recuperamos el parámetro de salida 'idE' que devuelve el SP
        if (outParams.get("idE") != null) {
            return ((Number) outParams.get("idE")).longValue();
        } else {
            return null;
        }
    }

    /**
     * 2. MÉTODO DE BÚSQUEDA (SP 's')
     * Busca una encuesta específica (si idEncuesta != null) o todas (si es null).
     * Usa BeanPropertyRowMapper para convertir automáticamente el ResultSet a objetos Java.
     */
    @SuppressWarnings("unchecked")
    public List<EncuestaResultado> buscarEncuestaConOpcionesYVotos(Long idEncuesta) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("s")
                // Mapeamos el resultado del SP a la clase EncuestaResultado
                .returningResultSet("resultados", BeanPropertyRowMapper.newInstance(EncuestaResultado.class));

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("tabla", "encuesta");
        inParams.put("id1", idEncuesta); // Puede ser null

        Map<String, Object> out = jdbcCall.execute(inParams);
        
        // Obtenemos la lista ya mapeada
        return (List<EncuestaResultado>) out.get("resultados");
    }

    /**
     * Método de compatibilidad para listar todas (llama al método anterior con null)
     */
    public List<EncuestaResultado> listarTodasLasEncuestas() {
        return buscarEncuestaConOpcionesYVotos(null);
    }

    /**
     * 3. MÉTODO AUXILIAR: VERIFICAR SI EL USUARIO VOTÓ
     * Devuelve el ID de la opción que el usuario votó en esa encuesta (o null si no votó).
     */
    public Long obtenerOpcionVotadaPorUsuario(Long idEncuesta, Long idUsuario) {
        String sql = "SELECT v.idOpcion FROM votar_o v " +
                     "JOIN opcion_e o ON v.idOpcion = o.id " +
                     "WHERE o.idEncuesta = ? AND v.idUsuario = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, idEncuesta, idUsuario);
        } catch (Exception e) {
            return null; // No votó nada en esta encuesta
        }
    }
}